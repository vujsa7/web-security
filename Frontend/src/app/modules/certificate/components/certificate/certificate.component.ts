import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { CertificateFull } from '../../models/certificate-full.model';
import { Certificate } from '../../models/certificate.model';
import { CertificateService } from '../../services/certificate.service';

@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.scss']
})
export class CertificateComponent implements OnInit {

  isLoading: boolean = false;
  certificates: Certificate[] = [];
  certificateFull!: CertificateFull;
  isFullCertificateLoaded: boolean = false;
  isMessageDialogVisible: boolean = false;
  messageDialogTitle: string = "";
  messageDialogMessage: string = "";
  messageDialogButtonText: string = "";

  constructor(private spinner: NgxSpinnerService, private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.isLoading = true;
    this.spinner.show();
    this.certificateService.getUserCertificates().subscribe(
      data => {
        this.certificates = data;
        this.isLoading = false;
        this.spinner.hide();
      }
    );
  }


  hasCertificates(): boolean {
    if (this.certificates.length > 0) {
      return true;
    }
    return false;
  }

  onShowFullInfo(serialNumber: string){
    this.certificateService.getCertificateFullInfo(serialNumber).subscribe(
      data => {
        this.certificateFull = data;
        this.isFullCertificateLoaded = true;
      }
    );
  }

  onCertificateDialogClose(event: any){
    this.isFullCertificateLoaded = false;
  }

  onShowIsRevoked(serialNumber: string){
    this.certificateService.getCertificateRevocationStatus(serialNumber).subscribe(
      data => {
        let certificate : Certificate = data;
        this.messageDialogTitle = "Certificate revokation status";
        if(certificate.revoked){
          this.messageDialogMessage = "This certificate is revoked.";
        } else {
          this.messageDialogMessage = "This certificate is completely valid";
        }
        this.messageDialogButtonText = "Okay";
        this.isMessageDialogVisible = true;
      }
    );
  }

  onMessageDialogNotify(event: any){
    this.isMessageDialogVisible = false;
  }

}
