import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/authentication/auth.service';
import { environment } from 'src/environments/environment';
import { Certificate } from '../models/certificate.model';
import { SigningCertificate } from '../models/signing-certificate.model';

@Injectable({
  providedIn: 'any'
})
export class CertificateService {
  private baseUrl: string = environment.baseUrl;
  
  constructor(private http: HttpClient, private authService: AuthService) { }

  getAllCertificates(): Observable<Certificate[]>{
    return this.http.get<Certificate[]>(this.baseUrl + "certificates", { headers: this.authService.getHeader() });
  }

  getValidCertificatesForSigning(): Observable<SigningCertificate[]>{
    return this.http.get<SigningCertificate[]>(this.baseUrl + "validCertificates/" + this.authService.getTokenUsername(), { headers: this.authService.getHeader() });
  }
}
