import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CertificateFull } from '../../models/certificate-full.model';

@Component({
  selector: 'app-certificate-info-dialog',
  templateUrl: './certificate-info-dialog.component.html',
  styleUrls: ['./certificate-info-dialog.component.scss']
})
export class CertificateInfoDialogComponent implements OnInit {

  @Input() certificate!: CertificateFull;
  @Output() notify: EventEmitter<string> = new EventEmitter<string>(); 

  constructor() { }

  ngOnInit(): void {
  }

  closeDialog(): void{
    this.notify.emit('close');
  }

}
