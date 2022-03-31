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

    login(loginRequest: Object): Observable<any>{
        return this.http.post<any>(this.baseUrl + 'auth', loginRequest);
    }

    setToken(data: string): void {
        localStorage.setItem('jwtToken', data);
    }

    getHeader() : HttpHeaders{
        let token = localStorage.getItem('jwtToken');
        let header = new HttpHeaders().set('Content-Type', 'application/json');
        if(token != null){
            header = new HttpHeaders().set('Content-Type', 'application/json')
                                    .set('Authorization',  'Bearer ' + localStorage.getItem('jwtToken'));
        }
        return header;
    }

    getToken(): any {
        return localStorage.getItem('jwtToken');
    }

    getTokenRole(): any{
        let decodedToken = this.getDecodedAccessToken(localStorage.getItem('jwtToken') || '');
        if(decodedToken == null){
            return 'ROLE_UNSIGNED';
        }
        return decodedToken.role;
    }

    getTokenUsername(): any{
        let decodedToken = this.getDecodedAccessToken(localStorage.getItem('jwtToken') || '');
        if(decodedToken == null){
            return null;
        }
        return decodedToken.sub;
    }

    getDecodedAccessToken(token: string): any {
        try {
            return jwt_decode(token);
        } catch(Error) {
            return null;
        }
    }

}