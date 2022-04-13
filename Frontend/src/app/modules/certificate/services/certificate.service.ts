import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/core/authentication/auth.service';
import { environment } from 'src/environments/environment';
import { CertificateFull } from '../models/certificate-full.model';
import { Certificate } from '../models/certificate.model';
import { IssuingCertificate } from '../models/issuing-certificate.model';

@Injectable({
  providedIn: 'any'
})
export class CertificateService {
  private baseUrl: string = environment.baseUrl;
  
  constructor(private http: HttpClient, private authService: AuthService) { }

  getUserCertificates(): Observable<Certificate[]>{
    return this.http.get<Certificate[]>(this.baseUrl + "certificates/user", { headers: this.authService.getHeader() });
  }

  getCertificateFile(serialNumber: string): Observable<any>{
    let options: any = { 
      headers: this.authService.getHeader(),
      responseType: 'application/octet-stream' 
    }
    return this.http.get<any>(this.baseUrl + "certificates/" + serialNumber, options);
  }

  getAllCertificates(): Observable<Certificate[]>{
    return this.http.get<Certificate[]>(this.baseUrl + "certificates", { headers: this.authService.getHeader() });
  }

  getIssuingCertificates(): Observable<IssuingCertificate[]>{
    return this.http.get<IssuingCertificate[]>(this.baseUrl + "issuingCertificates/" + this.authService.getTokenUsername(), { headers: this.authService.getHeader() });
  }

  revokeCertificate(serialNumber: string): Observable<any>{
    return this.http.put<any>(this.baseUrl + "revoke/" + serialNumber, null, { headers: this.authService.getHeader() });
  }

  getCertificateFullInfo(serialNumber: string): Observable<CertificateFull>{
    return this.http.get<CertificateFull>(this.baseUrl + "certificateFull/" + serialNumber, { headers: this.authService.getHeader() });
  }

  getCertificateRevocationStatus(serialNumber: string):  Observable<Certificate>{
    return this.http.get<Certificate>(this.baseUrl + "revocationStatus/" + serialNumber, { headers: this.authService.getHeader() });
  }

  postRootCertificate(certificate: any, template?: any): Observable<any>{
    return this.http.post<any>(this.baseUrl + 'rootCertificates', certificate, this.getOptions(template));
  }

  postCertificate(certificate: any, template?: any): Observable<any>{
    return this.http.post<any>(this.baseUrl + 'certificates', certificate, this.getOptions(template));
  }

  private getOptions(certificateType: string): any {
    if(certificateType == 'custom'){
      return {
        headers: this.authService.getHeader(), 
        responseType: 'text'
      }
    }
    return {
      headers: this.authService.getHeader(), 
      params: new HttpParams().set('certificateTemplateType', certificateType), 
      responseType: 'text' as 'text'
    }
  }
}
