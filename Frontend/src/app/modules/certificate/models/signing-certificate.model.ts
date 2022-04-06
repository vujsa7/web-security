/** Certificate model class used to sign other certificates */ 
export class SigningCertificate{
    serialNumber: string = "";
    validFrom: Date = new Date();
    validTo: Date = new Date();

    constructor(serialNumber: string, validFrom: Date, validTo: Date){
        this.serialNumber = serialNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }    
}