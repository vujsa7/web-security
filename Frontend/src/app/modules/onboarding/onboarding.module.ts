import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { OnboardingRoutingModule } from './onboarding-routing.module';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { AllsafeLogoComponent } from './components/allsafe-logo/allsafe-logo.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    AllsafeLogoComponent
  ],
  imports: [
    CommonModule,
    OnboardingRoutingModule,
    SharedModule
  ]
})
export class OnboardingModule { }
