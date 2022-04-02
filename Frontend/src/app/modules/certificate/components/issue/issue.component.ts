import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/authentication/auth.service';

@Component({
  selector: 'app-issue',
  templateUrl: './issue.component.html',
  styleUrls: ['./issue.component.scss']
})
export class IssueComponent {

  subjectTypes: Array<String> = new Array();
  keyUsages: Array<String> = new Array();
  enhancedKeyUsages: Array<String> = new Array();
  
  constructor(private authService: AuthService) { 
    this.initializeSubjectTypes();

    this.keyUsages.push("Digital Signature");
    this.keyUsages.push("Key Encipherment");
    this.keyUsages.push("CRL Sign");
    this.keyUsages.push("Encipher Only");
    this.keyUsages.push("Decipher Only");

    this.enhancedKeyUsages.push("Client");
    this.enhancedKeyUsages.push("Server");
    this.enhancedKeyUsages.push("Email Protection");
    this.enhancedKeyUsages.push("Code Signing");
    this.enhancedKeyUsages.push("Any Extended Key Usage");
  }

  private initializeSubjectTypes() {
    this.subjectTypes.push("Certificate Authority");
    this.subjectTypes.push("End Entity");
  }

}
