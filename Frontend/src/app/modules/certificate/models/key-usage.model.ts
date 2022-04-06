export class KeyUsage{
    keyUsageName: string = "";
    keyUsageValue: Number = 128;

    constructor(keyUsageName: string, keyUsageValue: Number){
        this.keyUsageName = keyUsageName;
        this.keyUsageValue = keyUsageValue;
    }    
}