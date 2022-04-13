import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CertificateFull } from '../../models/certificate-full.model';
import { CertificateService } from '../../services/certificate.service';

@Component({
  selector: 'app-certificate-info-dialog',
  templateUrl: './certificate-info-dialog.component.html',
  styleUrls: ['./certificate-info-dialog.component.scss']
})
export class CertificateInfoDialogComponent {

  @Input() certificate!: CertificateFull;
  @Output() notify: EventEmitter<string> = new EventEmitter<string>(); 

  constructor(private certificateService: CertificateService) { }

  fetchIssuerCertificate(): void{
    if(this.certificate.serialNumber != this.certificate.issuerSerialNumber){
      this.certificateService.getCertificateFullInfo(this.certificate.issuerSerialNumber).subscribe(
        data => {
          this.certificate = data;
        }
      );
    }
  }

  closeDialog(): void{
    this.notify.emit('close');
  }

}
