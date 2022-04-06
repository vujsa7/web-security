import { Component, OnInit } from '@angular/core';
import { KeyUsageService } from '../../services/key-usage.service';
import { KeyUsage } from '../../models/key-usage.model';
import { CertificateService } from '../../services/certificate.service';
import { SigningCertificate } from '../../models/signing-certificate.model';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-issue',
  templateUrl: './issue.component.html',
  styleUrls: ['./issue.component.scss']
})
export class IssueComponent implements OnInit {

  issueCertificateForm!: FormGroup;
  subjectTypes: Array<String> = new Array();
  keyUsages: Array<KeyUsage> = new Array();
  extendedKeyUsages: Array<string> = new Array();

  signingCertificates: SigningCertificate[] = new Array();

  validFromMin!: NgbDateStruct;
  validFromMax!: NgbDateStruct;
  validToMin!: NgbDateStruct;
  validToMax!: NgbDateStruct;

  constructor(private keyUsageService: KeyUsageService, private certificateService: CertificateService) {
    this.initializeSubjectTypes();
  }

  ngOnInit(): void {

    forkJoin({
      // Fetching key usages from backend
      keyUsages: this.keyUsageService.getKeyUsages(),
      // Fetching extended key usages from backend
      extendedKeyUsages: this.keyUsageService.getExtendedKeyUsages(),
      // Fetching certificates that can be used for signing
      signingCertificates: this.certificateService.getValidCertificatesForSigning()
    })
      .subscribe(
        data => {
          this.keyUsages = data.keyUsages;
          this.extendedKeyUsages = data.extendedKeyUsages;
          this.signingCertificates = data.signingCertificates;
          this.initilizeIssueCertificateForm();
        }
      );
  }

  signWithChanged(): void {
    if (this.issueCertificateForm.get("validFrom")?.disabled || this.issueCertificateForm.get("validTo")?.disabled) {
      this.issueCertificateForm.get("validFrom")?.enable();
      this.issueCertificateForm.get("validTo")?.enable();
    }
    this.setupAvailableValidityDates();
  }

  validFromChanged(): void {
    let validFrom = this.issueCertificateForm.get("validFrom")?.value;
    let validFromDate = new Date(validFrom.year, validFrom.month, validFrom.day);
    this.validToMin = { year: validFromDate.getFullYear(), month: validFromDate.getMonth(), day: validFromDate.getDate() };
  }

  validToChanged(): void {
    let validTo = this.issueCertificateForm.get("validTo")?.value;
    let validToDate = new Date(validTo.year, validTo.month, validTo.day);
    this.validFromMax = { year: validToDate.getFullYear(), month: validToDate.getMonth(), day: validToDate.getDate() };
  }

  keyUsageChanged(event: any, value: Number) {
    const selectedKeyUsages = (this.issueCertificateForm.controls.keyUsages as FormArray);
    if (event.target.checked) {
      selectedKeyUsages.push(new FormControl(value));
    } else {
      const index = selectedKeyUsages.controls
        .findIndex(x => x.value === value);
      selectedKeyUsages.removeAt(index);
    }
  }

  extendedKeyUsageChanged(event: any, value: string) {
    const selectedExtendedKeyUsages = (this.issueCertificateForm.controls.extendedKeyUsages as FormArray);
    if (event.target.checked) {
      selectedExtendedKeyUsages.push(new FormControl(value));
    } else {
      const index = selectedExtendedKeyUsages.controls
        .findIndex(x => x.value === value);
      selectedExtendedKeyUsages.removeAt(index);
    }
  }

  onSubmit(): void {
    if(this.issueCertificateForm.valid){
      let issueCertificateRequest = {
        email: this.issueCertificateForm.get("email")?.value,
        commonName: this.issueCertificateForm.get("commonName")?.value,
        country: this.issueCertificateForm.get("country")?.value,
        state: this.issueCertificateForm.get("state")?.value,
        local: this.issueCertificateForm.get("local")?.value,
        signWith: this.issueCertificateForm.get("signWith")?.value.serialNumber,
        validFrom: this.issueCertificateForm.get("validFrom")?.value,
        validTo: this.issueCertificateForm.get("validTo")?.value,
        ca: this.issueCertificateForm.get("subjectType")?.value == "Certificate Authority" ? true : false,
        keyUsages: this.issueCertificateForm.get("keyUsages")?.value,
        extendedKeyUsages: this.issueCertificateForm.get("extendedKeyUsages")?.value,
      };
    }
  }

  private initializeSubjectTypes(): void {
    this.subjectTypes.push("Certificate Authority");
    this.subjectTypes.push("End Entity");
  }

  private initilizeIssueCertificateForm(): void {
    this.issueCertificateForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.email]),
      commonName: new FormControl('', [Validators.required]),
      country: new FormControl('', [Validators.required]),
      state: new FormControl('', [Validators.required]),
      local: new FormControl('', [Validators.required]),
      signWith: new FormControl(null, [Validators.required]),
      validFrom: new FormControl({ value: null, disabled: true }, [Validators.required]),
      validTo: new FormControl({ value: null, disabled: true }, [Validators.required]),
      subjectType: new FormControl("Certificate Authority", [Validators.required]),
      keyUsages: new FormArray([], [Validators.minLength(1)]),
      extendedKeyUsages: new FormArray([], [Validators.minLength(1)])
    });
  }

  private setupAvailableValidityDates(): void {
    this.issueCertificateForm.get("validFrom")?.setValue(null);
    this.issueCertificateForm.get("validTo")?.setValue(null);
    let validFromDate = new Date(this.issueCertificateForm.controls["signWith"].value.validFrom);
    let validToDate = new Date(this.issueCertificateForm.controls["signWith"].value.validTo);
    this.validFromMin = { year: validFromDate.getFullYear(), month: validFromDate.getMonth() + 1, day: validFromDate.getDate() };
    this.validFromMax = { year: validToDate.getFullYear(), month: validToDate.getMonth() + 1, day: validToDate.getDate() };
    this.validToMax = { year: validToDate.getFullYear(), month: validToDate.getMonth() + 1, day: validToDate.getDate() };
    this.validToMin = { year: validFromDate.getFullYear(), month: validFromDate.getMonth() + 1, day: validFromDate.getDate() };
  }

}
