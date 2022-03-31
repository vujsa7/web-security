import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PrimaryButtonComponent } from './components/primary-button/primary-button.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxSpinnerModule } from 'ngx-spinner';


@NgModule({
  declarations: [
    PrimaryButtonComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ],
  exports:[
    PrimaryButtonComponent,
    ReactiveFormsModule,
    FormsModule,
    NgxSpinnerModule
  ]
})
export class SharedModule { }
