import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CertificateRoutingModule } from './certificate-routing.module';
import { CertificateComponent } from './components/certificate/certificate.component';
import { SharedModule } from 'src/app/shared/shared.module';

import { IssueComponent } from './components/issue/issue.component';
import { RevokeComponent } from './components/revoke/revoke.component';
import { KeyUsageService } from './services/key-usage.service';
import { CertificateService } from './services/certificate.service';
import { NgbDatepickerModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    CertificateComponent,
    IssueComponent,
    RevokeComponent
  ],
  imports: [
    CommonModule,
    CertificateRoutingModule,
    SharedModule,
    NgbDatepickerModule
  ],
  providers: [
    KeyUsageService,
    CertificateService,
    
  ]
})
export class CertificateModule { }
