export class Certificate{
    serialNumber: string = "";
    subjectName: string = "";
    issuerName: string = "";
    validFrom: Date = new Date();
    validTo: Date = new Date();
    revoked: boolean;

    constructor(serialNumber: string, subjectName: string, issuerName: string , validFrom: Date, validTo: Date, revoked: boolean){
        this.serialNumber = serialNumber;
        this.subjectName = subjectName;
        this.issuerName = issuerName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.revoked = revoked;
    }    
}