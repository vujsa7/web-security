import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from 'src/app/core/authentication/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;

  constructor(private toastr: ToastrService, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm() {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', [Validators.required]),
    });
  }

  onSubmit() : void{
    if(this.loginForm.valid){
      this.authService.login(this.loginForm.value).subscribe(
        data => {
          this.authService.setToken(data.accessToken);
          this.router.navigate(['/certificates']);
        },
        error => {
          if(error.status == 401)
            this.toastr.error("The email and password you entered didn't match our records. Please try again.", "Incorrect credentials");
        }
      );
    }
  }

}
