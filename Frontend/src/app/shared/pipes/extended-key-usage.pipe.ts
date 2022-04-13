import { Pipe, PipeTransform } from "@angular/core";

@Pipe({ 
    name: 'extendedKeyUsage' 
})
export class ExtendedKeyUsagePipe implements PipeTransform {

    transform(value: string, ...args: any[]) {
        if (value == "clientAuth" || value == "1.3.6.1.5.5.7.3.2")
            return "Client Authentication (1.3.6.1.5.5.7.3.2)";
        else if (value == "serverAuth" || value == "1.3.6.1.5.5.7.3.1")
            return "Server Authentication (1.3.6.1.5.5.7.3.1)";
        return "";
    }
}