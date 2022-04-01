import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PrimaryButtonComponent } from './components/primary-button/primary-button.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxSpinnerModule } from 'ngx-spinner';
import { CertificateCardComponent } from './components/certificate-card/certificate-card.component';
import { HeaderComponent } from './layout/header/header.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    PrimaryButtonComponent,
    CertificateCardComponent,
    HeaderComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ],
  exports:[
    // My declared components
    PrimaryButtonComponent,
    CertificateCardComponent,
    HeaderComponent,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ]
})
export class SharedModule { }
