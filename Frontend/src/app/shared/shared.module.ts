import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PrimaryButtonComponent } from './components/primary-button/primary-button.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxSpinnerModule } from 'ngx-spinner';
import { CertificateCardComponent } from './components/certificate-card/certificate-card.component';
import { HeaderComponent } from './layout/header/header.component';
import { RouterModule } from '@angular/router';
import { KeyUsagePipe } from './pipes/key-usage.pipe';
import { ExtendedKeyUsagePipe } from './pipes/extended-key-usage.pipe';
import { MessageDialogComponent } from './components/message-dialog/message-dialog.component';


@NgModule({
  declarations: [
    // My components
    PrimaryButtonComponent,
    CertificateCardComponent,
    HeaderComponent,
    MessageDialogComponent,

    // My custom pipes
    KeyUsagePipe,
    ExtendedKeyUsagePipe
  ],
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ],
  exports:[

    // My components
    PrimaryButtonComponent,
    CertificateCardComponent,
    HeaderComponent,
    MessageDialogComponent,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule,

    // My custom pipes
    KeyUsagePipe,
    ExtendedKeyUsagePipe
  ]
})
export class SharedModule { }
