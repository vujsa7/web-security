import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import jwt_decode from 'jwt-decode';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private baseUrl: string = environment.baseUrl;

    constructor(private http: HttpClient) { }

    login(loginRequest: Object): Observable<any> {
        return this.http.post<any>(this.baseUrl + 'auth', loginRequest);
    }

    register(registerRequest: Object): Observable<any>{
        return this.http.post<any>(this.baseUrl + 'users', registerRequest);
    }

    setToken(data: string): void {
        localStorage.setItem('jwtToken', data);
    }

    flushToken(): void{
        localStorage.removeItem('jwtToken');
    }

    getHeader(): HttpHeaders {
        let token = localStorage.getItem('jwtToken');
        let header = new HttpHeaders().set('Content-Type', 'application/json');
        if (token != null) {
            header = new HttpHeaders().set('Content-Type', 'application/json')
                .set('Authorization', 'Bearer ' + localStorage.getItem('jwtToken'));
        }
        return header;
    }

    hasValidToken(): boolean{
        let token = this.getToken();
        if(token == null){
            return false;
        } else {
            let decodedToken = this.getDecodedAccessToken(token);
            if(decodedToken.exp < Date.now() / 1000){
                this.flushToken();
                return false;
            }
        }
        return true;
    }

    getTokenRole(): any {
        var token = this.getToken();
        if(token == null){
            return 'ROLE_UNSIGNED';
        } else {
            let decodedToken = this.getDecodedAccessToken(token);
            return decodedToken.role;
        }
    }

    getTokenUsername(): any {
        var token = this.getToken();
        if(token == null){
            return null;
        } else {
            let decodedToken = this.getDecodedAccessToken(token);
            return decodedToken.sub;
        }
    }

    private getDecodedAccessToken(token: string): any {
        try {
            return jwt_decode(token);
        } catch (Error) {
            return null;
        }
    }

    public getToken(): any {
        var token = localStorage.getItem('jwtToken');
        return token;
    }

}