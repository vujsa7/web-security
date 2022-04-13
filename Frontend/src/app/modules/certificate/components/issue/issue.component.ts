import { Component, OnInit } from '@angular/core';
import { KeyUsageService } from '../../services/key-usage.service';
import { KeyUsage } from '../../models/key-usage.model';
import { CertificateService } from '../../services/certificate.service';
import { IssuingCertificate } from '../../models/issuing-certificate.model';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { AuthService } from 'src/app/core/authentication/auth.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ToastrService } from 'ngx-toastr';

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

  issuingCertificates: IssuingCertificate[] = new Array();

  validFromMin!: NgbDateStruct;
  validFromMax!: NgbDateStruct;
  validToMin!: NgbDateStruct;
  validToMax!: NgbDateStruct;

  isCreatingSelfSignedSignature: boolean = false;
  isLoading: boolean = false;

  certificateType: string = 'custom';

  constructor(private spinner: NgxSpinnerService, private keyUsageService: KeyUsageService, private certificateService: CertificateService, private authService: AuthService, private toastr: ToastrService) {
    this.initializeSubjectTypes();
  }

  ngOnInit(): void {

    this.isLoading = true;
    this.spinner.show();
    forkJoin({
      // Fetching key usages from backend
      keyUsages: this.keyUsageService.getKeyUsages(),
      // Fetching extended key usages from backend
      extendedKeyUsages: this.keyUsageService.getExtendedKeyUsages(),
      // Fetching certificates that can be used for signing
      signingCertificates: this.certificateService.getIssuingCertificates()
    })
      .subscribe(
        data => {
          this.keyUsages = data.keyUsages;
          this.extendedKeyUsages = data.extendedKeyUsages;
          this.issuingCertificates = data.signingCertificates;
          this.initilizeIssueCertificateForm();
          this.isLoading = false;
          this.spinner.hide();
        }
      );
  }

  signWithChanged(): void {
    this.enableValidityFields();
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

  templateChanged(event: any, certificateType: string): void {
    this.certificateType = certificateType;

    if(certificateType == 'rootCa'){
      this.issueCertificateForm.get("subjectType")?.setValue("Root CA (self signed)");
      this.subjectTypeChanged();

      const selectedKeyUsages = (this.issueCertificateForm.controls.keyUsages as FormArray);
      selectedKeyUsages.clear();
      selectedKeyUsages.push(new FormControl(128));
      selectedKeyUsages.push(new FormControl(4));
      selectedKeyUsages.push(new FormControl(32));
      selectedKeyUsages.push(new FormControl(2));
    } else if(certificateType == 'intermediateCa'){
      this.issueCertificateForm.get("subjectType")?.setValue("Intermediate CA");
      this.subjectTypeChanged();

      const selectedKeyUsages = (this.issueCertificateForm.controls.keyUsages as FormArray);
      selectedKeyUsages.clear();
      selectedKeyUsages.push(new FormControl(128));
      selectedKeyUsages.push(new FormControl(4));
      selectedKeyUsages.push(new FormControl(32));
      selectedKeyUsages.push(new FormControl(2));
    } else if(certificateType == 'endEntity'){
      this.issueCertificateForm.get("subjectType")?.setValue("End Entity");
      this.subjectTypeChanged();

      const selectedKeyUsages = (this.issueCertificateForm.controls.keyUsages as FormArray);
      selectedKeyUsages.clear();
      selectedKeyUsages.push(new FormControl(128));
    }
  }

  subjectTypeChanged(): void {
    let selectedSubjectType = this.issueCertificateForm.get("subjectType")?.value;
    if (selectedSubjectType.includes("Root CA")) {
      this.isCreatingSelfSignedSignature = true;
      this.enableValidityFields();
      this.disableSignWithField();
      this.setupAvailableValidityDates();
    }
    else {
      this.isCreatingSelfSignedSignature = false;
      this.enableSignWithField();
      if (this.issueCertificateForm.get("signWith")?.value == null)
        this.disableValidityDates();
    }
  }

  onSubmit(): void {
    if (this.issueCertificateForm.valid) {
      if (this.isCreatingSelfSignedSignature) {
        let issueCertificateRequest = {
          email: this.issueCertificateForm.get("email")?.value,
          commonName: this.issueCertificateForm.get("commonName")?.value,
          country: this.issueCertificateForm.get("country")?.value,
          state: this.issueCertificateForm.get("state")?.value,
          local: this.issueCertificateForm.get("local")?.value,
          validFrom: this.convertNgbDateToDate(this.issueCertificateForm.get("validFrom")?.value),
          validTo: this.convertNgbDateToDate(this.issueCertificateForm.get("validTo")?.value),
          ca: this.issueCertificateForm.get("subjectType")?.value == "End Entity" ? false : true,
          keyUsage: this.issueCertificateForm.get("keyUsages")?.value,
          extendedKeyUsage: this.issueCertificateForm.get("extendedKeyUsages")?.value,
        };
        console.log(issueCertificateRequest)
        this.certificateService.postRootCertificate(issueCertificateRequest, this.certificateType).subscribe(
          data => {
            this.toastr.success("Successfully generated certificate.", "Success");
            this.initilizeIssueCertificateForm();
          }, error => {
            this.toastr.error("Something went wrong.", "Error");
          }
        );
      } else {
        let issueCertificateRequest = {
          email: this.issueCertificateForm.get("email")?.value,
          commonName: this.issueCertificateForm.get("commonName")?.value,
          country: this.issueCertificateForm.get("country")?.value,
          state: this.issueCertificateForm.get("state")?.value,
          local: this.issueCertificateForm.get("local")?.value,
          signWith: this.issueCertificateForm.get("signWith")?.value.serialNumber,
          validFrom: this.convertNgbDateToDate(this.issueCertificateForm.get("validFrom")?.value),
          validTo: this.convertNgbDateToDate(this.issueCertificateForm.get("validTo")?.value),
          ca: this.issueCertificateForm.get("subjectType")?.value == "End Entity" ? false : true,
          keyUsage: this.issueCertificateForm.get("keyUsages")?.value,
          extendedKeyUsage: this.issueCertificateForm.get("extendedKeyUsages")?.value,
        };
        this.certificateService.postCertificate(issueCertificateRequest, this.certificateType).subscribe(
          data => {
            this.toastr.success("Successfully generated certificate.", "Success");
            this.initilizeIssueCertificateForm();
          }, error => {
            this.toastr.error("Something went wrong.", "Error");
          }
        );
      }
    }
  }

  hasIssuingCertificates(): boolean{
    if(this.issuingCertificates.length > 0){
      return true;
    }
    return false;
  }

  private initializeSubjectTypes(): void {
    this.subjectTypes.push("Intermediate CA");
    this.subjectTypes.push("End Entity");
    if (this.authService.getTokenRole() == "ROLE_ADMIN") {
      this.subjectTypes.push("Root CA (self signed)")
    }
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
      subjectType: new FormControl("Intermediate CA", [Validators.required]),
      keyUsages: new FormArray([], [Validators.minLength(1)]),
      extendedKeyUsages: new FormArray([], [Validators.minLength(1)])
    });
  }

  private setupAvailableValidityDates(): void {
    if (this.isCreatingSelfSignedSignature) {
      this.validFromMin = { year: 1972, month: 1, day: 1 };
      this.validFromMax = { year: 2050, month: 12, day: 12 };
      this.validToMax = { year: 2050, month: 12, day: 12 };
      this.validToMin = { year: 1972, month: 1, day: 1 };
    } else {
      let validFromDate = new Date(this.issueCertificateForm.controls["signWith"].value.validFrom);
      let validToDate = new Date(this.issueCertificateForm.controls["signWith"].value.validTo);
      this.validFromMin = { year: validFromDate.getFullYear(), month: validFromDate.getMonth() + 1, day: validFromDate.getDate() };
      this.validFromMax = { year: validToDate.getFullYear(), month: validToDate.getMonth() + 1, day: validToDate.getDate() };
      this.validToMax = { year: validToDate.getFullYear(), month: validToDate.getMonth() + 1, day: validToDate.getDate() };
      this.validToMin = { year: validFromDate.getFullYear(), month: validFromDate.getMonth() + 1, day: validFromDate.getDate() };
    }
  }

  private enableValidityFields() {
    if (this.issueCertificateForm.get("validFrom")?.disabled || this.issueCertificateForm.get("validTo")?.disabled) {
      this.issueCertificateForm.get("validFrom")?.enable();
      this.issueCertificateForm.get("validTo")?.enable();
    }
  }

  private disableValidityDates() {
    if (this.issueCertificateForm.get("validFrom")?.enabled || this.issueCertificateForm.get("validTo")?.enabled) {
      this.issueCertificateForm.get("validFrom")?.disable();
      this.issueCertificateForm.get("validTo")?.disable();
      this.issueCertificateForm.get("validFrom")?.setValue(null);
      this.issueCertificateForm.get("validTo")?.setValue(null);
    }
  }

  private enableSignWithField() {
    if (this.issueCertificateForm.get("signWith")?.disabled)
      this.issueCertificateForm.get("signWith")?.enable();
  }

  private disableSignWithField() {
    if (this.issueCertificateForm.get("signWith")?.enabled) {
      this.issueCertificateForm.get("signWith")?.disable();
      this.issueCertificateForm.get("signWith")?.setValue(null);
    }
  }

  private convertNgbDateToDate(ngbDate: any): Date {
    let date = new Date(ngbDate.year, ngbDate.month - 1, ngbDate.day);

    return date;
  }

}
