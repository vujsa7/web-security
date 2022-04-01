import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CertificateComponent } from './components/certificate/certificate.component';
import { IssueComponent } from './components/issue/issue.component';
import { RevokeComponent } from './components/revoke/revoke.component';

const routes: Routes = [
  { path: '', component: CertificateComponent },
  { path: 'issue', component: IssueComponent},
  { path: 'revoke', component: RevokeComponent},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CertificateRoutingModule { }
