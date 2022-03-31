import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: 'onboarding', loadChildren: () => import('./modules/onboarding/onboarding.module').then(m => m.OnboardingModule) },
  { path: 'certificates', loadChildren: () => import('./modules/certificate/certificate.module').then(m => m.CertificateModule) },
  { path: '**', redirectTo: 'onboarding' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
