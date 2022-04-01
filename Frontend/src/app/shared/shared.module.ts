import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PrimaryButtonComponent } from './components/primary-button/primary-button.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxSpinnerModule } from 'ngx-spinner';
import { CertificateCardComponent } from './components/certificate-card/certificate-card.component';


@NgModule({
  declarations: [
    PrimaryButtonComponent,
    CertificateCardComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ],
  exports:[
    PrimaryButtonComponent,
    CertificateCardComponent,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ]
})
export class SharedModule { }
