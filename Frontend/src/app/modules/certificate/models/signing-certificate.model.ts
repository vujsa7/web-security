/** Certificate model class used to sign other certificates */ 
export class SigningCertificate{
    serialNumber: string = "";
    commonName: string = "";
    validFrom: Date = new Date();
    validTo: Date = new Date();

    constructor(serialNumber: string, commonName: string, validFrom: Date, validTo: Date){
        this.serialNumber = serialNumber;
        this.commonName = commonName;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }    
}