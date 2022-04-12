import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Observable } from "rxjs";
import { AuthService } from "../authentication/auth.service";

@Injectable()
export class AuthenticationGuard implements CanActivate{

    constructor(private authService: AuthService, private router: Router, private toastr: ToastrService){}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        if (!this.authService.hasValidToken()){
            this.router.navigateByUrl('/onboarding/login');
            this.toastr.info("Please log in again to continue.", "Session expired");
            return false;
        }
        return true;
    }

}
