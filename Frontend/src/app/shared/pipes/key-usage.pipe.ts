import { Pipe, PipeTransform } from "@angular/core";

@Pipe({ 
    name: 'keyUsage' 
})
export class KeyUsagePipe implements PipeTransform {

    transform(value: Number, ...args: any[]) {
        if (value == 128)
            return "Digital Signature";
        else if (value == 64)
            return "Non Repudiation";
        else if (value == 32)
            return "Key Encipherment";
        else if (value == 16)
            return "Data Encipherment";
        else if (value == 8)
            return "Key Agreement";
        else if (value == 4)
            return "Key Certificate Sign";
        else if (value == 2)
            return "cRL Sign";
        else if (value == 1)
            return "Encipher Only";
        else if (value == 32768)
            return "Decipher Only";
        return "";
    }

}