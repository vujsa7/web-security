import { Pipe, PipeTransform } from "@angular/core";

@Pipe({ 
    name: 'keyUsage' 
})
export class KeyUsagePipe implements PipeTransform {

    transform(value: string, ...args: any[]) {
        if (value == "digitalSignature")
            return "Digital Signature";
        else if (value == "nonRepudiation")
            return "Non Repudiation";
        else if (value == "keyEncipherment")
            return "Key Encipherment";
        else if (value == "dataEncipherment")
            return "Data Encipherment";
        else if (value == "keyAgreement")
            return "Key Agreement";
        else if (value == "keyCertSign")
            return "Key Certificate Sign";
        else if (value == "cRLSign")
            return "cRL Sign";
        else if (value == "encipherOnly")
            return "Encipher Only";
        else if (value == "decipherOnly")
            return "Decipher Only";
        return "";
    }

}