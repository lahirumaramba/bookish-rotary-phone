using System;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using FirebaseAdmin;
using FirebaseAdmin.Auth;
using Google.Apis.Auth.OAuth2;
using Google.Cloud.Firestore;
using System.Collections.Generic;

namespace FirebaseAdminTest
{
    [TestClass]
    public class FirebaseAdminTests
    {
        private static FirebaseApp? _app;

        [ClassInitialize]
        public static void Setup(TestContext context)
        {
            var options = new AppOptions
            {
                Credential = GoogleCredential.GetApplicationDefault(),
                ProjectId = "admin-sdk-wif",
                ServiceAccountId = "firebase-adminsdk-fbsvc@admin-sdk-wif.iam.gserviceaccount.com"
            };

            _app = FirebaseApp.Create(options);
        }

        [TestMethod]
        public void Test1InitializeApp()
        {
            Assert.IsNotNull(_app);
            Console.WriteLine("Firebase Admin SDK initialized successfully via ADC");
        }

        [TestMethod]
        public async Task Test2AuthGetUsers()
        {
            var auth = FirebaseAuth.GetAuth(_app);
            Assert.IsNotNull(auth);

            try
            {
                var pagedEnumerable = auth.ListUsersAsync(new ListUsersOptions { MaxResults = 1 });
                var enumerator = pagedEnumerable.GetAsyncEnumerator();
                bool hasNext = await enumerator.MoveNextAsync();

                if (hasNext)
                {
                    Console.WriteLine("Successfully fetched users.");
                }
                else
                {
                    Console.WriteLine("No users fetched.");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Auth test (ListUsersAsync) failed: {ex.Message}");
                throw;
            }
        }

        [TestMethod]
        public async Task Test3FirestoreRead()
        {
            var db = await FirestoreDb.CreateAsync("admin-sdk-wif");
            Assert.IsNotNull(db);

            try
            {
                var docRef = db.Collection("wif-demo").Document("test-connection");
                var snapshot = await docRef.GetSnapshotAsync();

                Assert.IsTrue(snapshot.Exists);

                var data = snapshot.ToDictionary();
                Assert.IsNotNull(data);
                Assert.AreEqual("Hello from GitHub Actions WIF!", data["message"]);
                Assert.IsTrue(data.ContainsKey("timestamp"));
                
                Console.WriteLine("Firestore read successful.");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Firestore test failed: {ex.Message}");
                throw;
            }
        }

        [TestMethod]
        public async Task Test4CreateCustomToken()
        {
            var auth = FirebaseAuth.GetAuth(_app);
            var uid = "wif-demo-user-123";

            try
            {
                var customToken = await auth.CreateCustomTokenAsync(uid);
                Assert.IsFalse(string.IsNullOrEmpty(customToken));
                Console.WriteLine("Successfully created a custom signed token.");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Token signing test failed: {ex.Message}");
                throw;
            }
        }
    }
}
