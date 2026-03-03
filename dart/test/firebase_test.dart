import 'dart:io';
import 'package:test/test.dart';
import 'package:dart_firebase_admin/dart_firebase_admin.dart';
import 'package:dart_firebase_admin/auth.dart';
import 'package:dart_firebase_admin/firestore.dart';

void main() {
  group('Firebase Admin Dart SDK WIF Demo', () {
    late FirebaseApp app;

    setUpAll(() async {
      // Initialize via WIF (ADC)
      app = FirebaseApp.initializeApp(
        name: 'bookish-rotary-phone-dart',
        options: AppOptions(
          credential: Credential.fromApplicationDefaultCredentials(),
          serviceAccountId: 'firebase-adminsdk-fbsvc@admin-sdk-wif.iam.gserviceaccount.com',
          projectId: 'admin-sdk-wif',
        )
      );
    });

    test('1. should initializeApp via WIF (ADC)', () {
      expect(app, isNotNull);
      print('Firebase Admin SDK initialized successfully via ADC');
    });

    test('2. should test Auth (getUsers)', () async {
      final auth = app.auth();
      expect(auth, isNotNull);

      try {
        final listUsersResult = await auth.listUsers(maxResults: 1);
        print('Successfully fetched \${listUsersResult.users.length} users.');
        expect(listUsersResult.users, isA<List>());
      } catch (error) {
        print('Auth test (listUsers) failed: \$error');
        rethrow;
      }
    });

    test('3. should test Firestore (simple read)', () async {
      final db = app.firestore();
      expect(db, isNotNull);

      try {
        final docRef = db.collection('wif-demo').doc('test-connection');
        final testData = {
          'timestamp': DateTime.now().toIso8601String(),
          'message': 'Hello from GitHub Actions WIF!',
        };

        final doc = await docRef.get();
        expect(doc.exists, isTrue);
        expect(doc.data(), isNotNull);
        expect(doc.data()?['message'], equals(testData['message']));
        expect(doc.data()?['timestamp'], isNotNull);
        print('Firestore read successful.');
      } catch (error) {
        print('Firestore test failed: \$error');
        rethrow;
      }
    });

    test('4. should test signed tokens (createCustomToken)', () async {
      final auth = app.auth();
      final uid = 'wif-demo-user-123';

      try {
        final customToken = await auth.createCustomToken(uid);
        expect(customToken, isNotNull);
        expect(customToken, isA<String>());
        print('Successfully created a custom signed token.');
      } catch (error) {
        print('Token signing test failed: \$error');
        rethrow;
      }
    });
  });
}
