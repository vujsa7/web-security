import { Component, Input, OnInit } from '@angular/core';
import { Certificate } from 'src/app/modules/certificate/models/certificate.model';

@Component({
  selector: 'app-certificate-card',
  templateUrl: './certificate-card.component.html',
  styleUrls: ['./certificate-card.component.scss']
})
export class CertificateCardComponent implements OnInit {
  @Input() mode!: string;
  @Input() certificate!: Certificate;

  constructor() { }

  ngOnInit(): void {
  }

}
