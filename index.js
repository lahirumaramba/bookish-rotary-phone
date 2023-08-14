//import { GoogleAuth } from 'google-auth-library';
import { initializeApp } from 'firebase-admin/app';
import { getAuth } from 'firebase-admin/auth';

async function main() {
    const app = initializeApp({ projectId: 'admin-sdk-8b7ba' });
    console.log(await app.options.credential.getAccessToken());
    const user = await getAuth().getUserByEmail('email@email.com');
    console.log(user);
//   const auth = new GoogleAuth({
//     scopes: 'https://www.googleapis.com/auth/cloud-platform',
//   });
//   const client = await auth.getClient();
//   const projectId = await auth.getProjectId();
//   console.log(projectId);
//   const url = `https://dns.googleapis.com/dns/v1/projects/${projectId}`;
//   const res = await client.request({ url });
//   console.log(res.data);
}

main().catch((e) => console.log(e));
