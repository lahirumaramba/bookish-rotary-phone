import pytest
from firebase_admin import credentials, initialize_app, auth, firestore

@pytest.fixture(scope="module")
def app():
    # Use ADC
    cred = credentials.ApplicationDefault()
    return initialize_app(cred)

def test_initialize_app(app):
    assert app is not None
    print("Firebase Admin SDK initialized successfully via ADC")

def test_auth_get_users(app):
    try:
        # Fetch users (limited to 1 for the test)
        page = auth.list_users(max_results=1, app=app)
        users = list(page.users)
        print(f"Successfully fetched {len(users)} users.")
        assert isinstance(users, list)
    except Exception as e:
        print(f"Auth test (list_users) failed: {e}")
        raise e

def test_firestore_read(app):
    db = firestore.client(app=app)
    assert db is not None
    try:
        doc_ref = db.collection('wif-demo').document('test-connection')
        doc = doc_ref.get()
        assert doc.exists
        
        data = doc.to_dict()
        assert data is not None
        assert data.get('message') == 'Hello from GitHub Actions WIF!'
        assert data.get('timestamp') is not None
        print('Firestore read successful.')
    except Exception as e:
        print(f"Firestore test failed: {e}")
        raise e

def test_create_custom_token(app):
    uid = 'wif-demo-user-123'
    try:
        custom_token = auth.create_custom_token(uid, app=app)
        assert custom_token is not None
        assert isinstance(custom_token, bytes) or isinstance(custom_token, str)
        print('Successfully created a custom signed token.')
    except Exception as e:
        print(f"Token signing test failed: {e}")
        # signing requires iam.serviceAccounts.signBlob permission
        raise e
