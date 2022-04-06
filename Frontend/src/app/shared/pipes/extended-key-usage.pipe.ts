import { Pipe, PipeTransform } from "@angular/core";

@Pipe({ 
    name: 'extendedKeyUsage' 
})
export class ExtendedKeyUsagePipe implements PipeTransform {

    transform(value: string, ...args: any[]) {
        if (value == "clientAuth")
            return "Client Authentication (1.3.6.1.5.5.7.3.2)";
        else if (value == "serverAuth")
            return "Server Authentication (1.3.6.1.5.5.7.3.1)";
        return "";
    }
}