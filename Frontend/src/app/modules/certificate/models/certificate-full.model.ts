import { KeyUsage } from "./key-usage.model";

export class CertificateFull{
    version: string = "";
    serialNumber: string = "";
    signatureAlgorithm: string = "";
    issuerCN: string = "";
    issuerO: string = "";
    issuerC: string = "";
    issuerS: string = "";
    issuerL: string = "";
    subjectCN: string = "";
    subjectO: string = "";
    subjectC: string = "";
    subjectS: string = "";
    subjectL: string = "";
    validFrom: Date = new Date();
    validTo: Date = new Date();
    subjectType: string = "";
    keyUsages: KeyUsage[] = [];
    extendedKeyUsages: string[] = [];
    publicKey: string = "";

    constructor(version: string, serialNumber: string, signatureAlgorithm: string, issuerCN: string,
        issuerO: string, issuerC: string, issuerS: string, issuerL: string, subjectCN: string, subjectO: string, subjectC: string,
        subjectS: string, subjectL: string, validFrom: Date, validTo: Date, subjectType: string, keyUsages: KeyUsage[], extendedKeyUsages: string[], publicKey: string){

        this.version = version;
        this.serialNumber = serialNumber;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuerCN = issuerCN;
        this.issuerO = issuerO;
        this.issuerC = issuerC;
        this.issuerS = issuerS;
        this.issuerL = issuerL;
        this.subjectCN = subjectCN;
        this.subjectO = subjectO;
        this.subjectC = subjectC;
        this.subjectS = subjectS;
        this.subjectL = subjectL;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.subjectType = subjectType;
        this.keyUsages = keyUsages;
        this.extendedKeyUsages= extendedKeyUsages;
        this.publicKey = publicKey;
    }    
}