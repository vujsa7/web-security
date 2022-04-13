import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Certificate } from 'src/app/modules/certificate/models/certificate.model';
import { CertificateService } from 'src/app/modules/certificate/services/certificate.service';

@Component({
  selector: 'app-certificate-card',
  templateUrl: './certificate-card.component.html',
  styleUrls: ['./certificate-card.component.scss']
})
export class CertificateCardComponent implements OnInit {
  @Input() mode!: string;
  @Input() certificate!: Certificate;
  @Output() showFullInfo = new EventEmitter<string>();
  @Output() showIsRevoked = new EventEmitter<string>();

  constructor(private toastr: ToastrService, private certificateService: CertificateService) { }

  ngOnInit(): void {
  }

  revokeCertificate(){
    if(this.certificate.revoked == false){
      this.certificateService.revokeCertificate(this.certificate.serialNumber).subscribe(
        data => {
          this.toastr.success('Certificate successfully revoked')
          this.certificate.revoked = true;
          return;
        },
        error => {
          this.toastr.error('There was an error while revoking the certificate')
        }
      )
    }
    else{
      this.toastr.error('This certificate has already been revoked')
    }
  }

  showFullInfoDialog(){
    this.showFullInfo.emit(this.certificate.serialNumber);
  }

  showIsRevokedDialog(){
    this.showIsRevoked.emit(this.certificate.serialNumber);
  }

}
