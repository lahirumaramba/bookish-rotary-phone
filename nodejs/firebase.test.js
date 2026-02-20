import { describe, it, expect, beforeAll } from 'vitest';
import { initializeApp } from 'firebase-admin/app';
import { getAuth } from 'firebase-admin/auth';
import { getFirestore } from 'firebase-admin/firestore';

describe('Firebase Admin Node.js SDK WIF Demo', () => {
    let app;

    beforeAll(() => {
        app = initializeApp();
    });

    it('1. should initializeApp via WIF (ADC)', () => {
        expect(app).toBeDefined();
        console.log('Firebase Admin SDK initialized successfully via ADC');
    });

    it('2. should test Auth (getUsers)', async () => {
        const auth = getAuth(app);
        expect(auth).toBeDefined();

        try {
            // Fetch users (limted to 1 for the test)
            const listUsersResult = await auth.listUsers(1);
            console.log(`Successfully fetched ${listUsersResult.users.length} users.`);
            expect(listUsersResult.users).toBeInstanceOf(Array);
        } catch (error) {
            console.error('Auth test (listUsers) failed:', error.message);
            throw error;
        }
    });

    it('3. should test Firestore (simple write/read)', async () => {
        const db = getFirestore(app);
        expect(db).toBeDefined();

        try {
            const docRef = db.collection('wif-demo').doc('test-connection');
            const testData = {
                timestamp: new Date().toISOString(),
                message: 'Hello from GitHub Actions WIF!'
            };

            const doc = await docRef.get();
            expect(doc.exists).toBe(true);
            expect(doc.data()).toBeDefined();
            expect(doc.data().message).toBe(testData.message);
            expect(doc.data().timestamp).toBe(testData.timestamp);
            console.log('Firestore read successful.');
        } catch (error) {
            console.error('Firestore test failed:', error.message);
            throw error;
        }
    });

    it('4. should test signed tokens (createCustomToken)', async () => {
        const auth = getAuth(app);
        const uid = 'wif-demo-user-123';

        try {
            const customToken = await auth.createCustomToken(uid);
            expect(customToken).toBeDefined();
            expect(typeof customToken).toBe('string');
            console.log('Successfully created a custom signed token.');
        } catch (error) {
            console.error('Token signing test failed:', error.message);
            // signing requires iam.serviceAccounts.signBlob permission
            throw error;
        }
    });
});
