import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CertificateRoutingModule } from './certificate-routing.module';
import { CertificateComponent } from './components/certificate/certificate.component';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [
    CertificateComponent
  ],
  imports: [
    CommonModule,
    CertificateRoutingModule,
    SharedModule
  ]
})
export class CertificateModule { }
