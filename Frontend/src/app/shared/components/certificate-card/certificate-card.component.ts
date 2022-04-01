import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-certificate-card',
  templateUrl: './certificate-card.component.html',
  styleUrls: ['./certificate-card.component.scss']
})
export class CertificateCardComponent implements OnInit {
  @Input() mode!: string;

  constructor() { }

  ngOnInit(): void {
  }

}
