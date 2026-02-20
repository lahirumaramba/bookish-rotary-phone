# Local Authentication with Firebase Admin SDK

To use `gcloud auth application-default login` with the Firebase Admin SDK, you must grant the `cloud-platform` scope. This allows the SDK to access Firebase services using your local user credentials.

## 1. Login with Scopes
Run the following command in your terminal:

```bash
gcloud auth application-default login --scopes="https://www.googleapis.com/auth/cloud-platform,https://www.googleapis.com/auth/userinfo.email"
```

## 2. Browser Authentication
1. A browser window will open. Select your Google account.
2. **Crucial**: You must check the box that allows permission to **"See, edit, configure, and delete your Google Cloud data"**.
3. Complete the login process.

## 3. Usage in Code
The SDK will now automatically find these credentials. You don't need to pass explicit credentials to `initializeApp()`:

```javascript
import { initializeApp } from 'firebase-admin/app';

// The SDK automatically uses local ADC
const app = initializeApp({
  projectId: 'your-project-id' // Replace with your actual project ID
});
```

## 4. Token Signing (Custom Tokens)
If you are using `createCustomToken()` and get a `Permission 'iam.serviceAccounts.signBlob' denied` error, your user account needs permission to "act as" the service account.

The error occurs because when you run `npm test` locally, the code is using your **personal identity** (via Application Default Credentials). When you call `createCustomToken`, the Firebase Admin SDK needs to sign a JWT. Since local ADC doesn't have the service account's private key, it tries to call the **IAM `signBlob` API** to sign the token on its behalf.

For this to work, your user account must have the **Service Account Token Creator** role granted on that specific service account resource.

### Configuration Steps:
1.  In Google Cloud Console, go to **IAM & Admin > Service Accounts**.
2.  Find the service account you are using (e.g., `firebase-adminsdk-fbsvc@...`).
3.  Click on the name to go to **Details**, then click the **Permissions** tab.
4.  Click **Grant Access**.
5.  Add your user email as the principal.
6.  Select the role **Service Account Token Creator**.

## 5. Why this is happening
*   **WIF in CI**: When this runs in GitHub Actions, the Workload Identity Federation (WIF) setup usually grants these permissions to the GitHub identity automatically.
*   **Local Dev**: Locally, the SDK sees `serviceAccountId` and tries to use your local ADC (gcloud) to sign for it. It fails because your personal user doesn't have the explicit right to sign blobs using that service account's identity until you grant the role mentioned above.

## 6. Troubleshooting
If you get an `invalid_grant` error:
- Ensure you checked the "Google Cloud data" permission box during browser login.
- Verify your local system time is synced.
- Try running the login command again with the `--scopes` flag.
