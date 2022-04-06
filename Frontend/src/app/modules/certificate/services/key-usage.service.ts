import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { KeyUsage } from '../models/key-usage.model';

@Injectable({
  providedIn: 'any'
})
export class KeyUsageService {
  private baseUrl: string = environment.baseUrl;

  constructor(private http: HttpClient) { }

  getKeyUsages(): Observable<KeyUsage[]>{
    return this.http.get<KeyUsage[]>(this.baseUrl + "keyUsage");
  }

  getExtendedKeyUsages(): Observable<string[]>{
    return this.http.get<string[]>(this.baseUrl + 'extendedKeyUsage');
  }
}
