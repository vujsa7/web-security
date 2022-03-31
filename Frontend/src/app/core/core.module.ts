import { CommonModule } from '@angular/common';
import { NgModule, Optional, SkipSelf } from '@angular/core';
import { AuthService } from './authentication/auth.service';
import { throwIfAlreadyLoaded } from './guards/import.guard';

@NgModule({
  imports: [
    CommonModule
  ],
  providers: [
    AuthService
  ]
})
export class CoreModule {
  //This way we are ensuring that CoreModule is imported only once in the AppModule
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}