import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
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

}
