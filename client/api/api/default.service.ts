/**
 * Base User API
 * Basic User API for other APIs
 *
 * The version of the OpenAPI document: 1.0
 * Contact: nikolaus@trixner.eu
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import {Inject, Injectable, Optional} from '@angular/core';
import {HttpClient, HttpEvent, HttpHeaders, HttpParameterCodec, HttpParams, HttpResponse} from '@angular/common/http';
import {CustomHttpParameterCodec} from '../encoder';
import {Observable} from 'rxjs';

import {ForgotPasswordDto} from '../model/forgotPasswordDto';
import {PaginationRequestDto} from '../model/paginationRequestDto';
import {PasswordResetDto} from '../model/passwordResetDto';
import {RegistrationDto} from '../model/registrationDto';
import {UserDto} from '../model/userDto';
import {UserListDto} from '../model/userListDto';

import {BASE_PATH} from '../variables';
import {Configuration} from '../configuration';


@Injectable({
  providedIn: 'root'
})
export class DefaultService {

    protected basePath = 'http://localhost:8080';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();
    public encoder: HttpParameterCodec;

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {
        if (configuration) {
            this.configuration = configuration;
        }
        if (typeof this.configuration.basePath !== 'string') {
            if (typeof basePath !== 'string') {
                basePath = this.basePath;
            }
            this.configuration.basePath = basePath;
        }
        this.encoder = this.configuration.encoder || new CustomHttpParameterCodec();
    }

    public confirmRegistration(token: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
        if (token === null || token === undefined) {
            throw new Error('Required parameter token was null or undefined when calling confirmRegistration.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        const httpHeaderAccepts: string[] = [];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        return this.httpClient.get<any>(`${this.configuration.basePath}/user/registration/confirmRegistration/${encodeURIComponent(String(token))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }


    /**
     * Confirms the registration of a new user by activating via a link that was sent via email.
     * @param token The registration token that was sent via mail to the new user\&#39;s address
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public confirmRegistration(token: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public confirmRegistration(token: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public confirmRegistration(token: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;

    /**
     * Your GET endpoint
     * Call if the user forgot their password and want to get sent a mail with a password change link
     * @param forgotPasswordDto
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public forgotPassword(forgotPasswordDto?: ForgotPasswordDto, observe?: 'body', reportProgress?: boolean): Observable<any>;

    /**
     * Logs a user in with username and password
     * @param username
     * @param password
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public loginUser(username: string, password: string, observe?: 'body', reportProgress?: boolean): Observable<any>;

    public forgotPassword(forgotPasswordDto?: ForgotPasswordDto, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public forgotPassword(forgotPasswordDto?: ForgotPasswordDto, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public forgotPassword(forgotPasswordDto?: ForgotPasswordDto, observe: any = 'body', reportProgress: boolean = false): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        const httpHeaderAccepts: string[] = [];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.get<any>(`${this.configuration.basePath}/user/forgotPassword`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Your GET endpoint
     * Returns the current user
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getCurrentUser(observe?: 'body', reportProgress?: boolean): Observable<UserDto>;
    public getCurrentUser(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserDto>>;
    public getCurrentUser(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserDto>>;
    public getCurrentUser(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // authentication (auth) required
        if (this.configuration.username || this.configuration.password) {
            headers = headers.set('Authorization', 'Basic ' + btoa(this.configuration.username + ':' + this.configuration.password));
        }
        // to determine the Accept header
        const httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        return this.httpClient.get<UserDto>(`${this.configuration.basePath}/user`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Get a specific user
     * Returns a specific user
     * @param userId The user ID of the user, or null for the currently logged in user
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getUserById(userId: string, observe?: 'body', reportProgress?: boolean): Observable<UserDto>;
    public getUserById(userId: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserDto>>;
    public getUserById(userId: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserDto>>;
    public getUserById(userId: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (userId === null || userId === undefined) {
            throw new Error('Required parameter userId was null or undefined when calling getUserById.');
        }

        let headers = this.defaultHeaders;

        // authentication (auth) required
        if (this.configuration.username || this.configuration.password) {
            headers = headers.set('Authorization', 'Basic ' + btoa(this.configuration.username + ':' + this.configuration.password));
        }
        // to determine the Accept header
        const httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        return this.httpClient.get<UserDto>(`${this.configuration.basePath}/user/${encodeURIComponent(String(userId))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Your GET endpoint
     * Returns the amount of users that currently exist.
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getUserCount(observe?: 'body', reportProgress?: boolean): Observable<number>;
    public getUserCount(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<number>>;
    public getUserCount(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<number>>;
    public getUserCount(observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // authentication (auth) required
        if (this.configuration.username || this.configuration.password) {
            headers = headers.set('Authorization', 'Basic ' + btoa(this.configuration.username + ':' + this.configuration.password));
        }
        // to determine the Accept header
        const httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        return this.httpClient.get<number>(`${this.configuration.basePath}/userlist/num`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Your GET endpoint
     * Return a paginated list of all users. If the provided pagination is not correct, the first 20 users will be returned instead.
     * @param paginationRequestDto The Pagination Request. Is ignored if null.
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public listUsers(paginationRequestDto?: PaginationRequestDto, observe?: 'body', reportProgress?: boolean): Observable<UserListDto>;
    public listUsers(paginationRequestDto?: PaginationRequestDto, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<UserListDto>>;
    public listUsers(paginationRequestDto?: PaginationRequestDto, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<UserListDto>>;
    public listUsers(paginationRequestDto?: PaginationRequestDto, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // authentication (auth) required
        if (this.configuration.username || this.configuration.password) {
            headers = headers.set('Authorization', 'Basic ' + btoa(this.configuration.username + ':' + this.configuration.password));
        }
        // to determine the Accept header
        const httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json',
            'application/xml'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.get<UserListDto>(`${this.configuration.basePath}/userlist`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    public loginUser(username: string, password: string, observe: any = 'body', reportProgress: boolean = false): Observable<any> {
        if (username === null || username === undefined) {
            throw new Error('Required parameter username was null or undefined when calling loginUser.');
        }
        if (password === null || password === undefined) {
            throw new Error('Required parameter password was null or undefined when calling loginUser.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        const httpHeaderAccepts: string[] = [];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/x-www-form-urlencoded'
        ];

        const canConsumeForm = this.canConsumeForm(consumes);

        let formParams: { append(param: string, value: any): any; };
        let useForm = false;
        let convertFormParamsToString = false;
        if (useForm) {
            formParams = new FormData();
        } else {
            formParams = new HttpParams({encoder: this.encoder});
        }

        if (username !== undefined) {
            formParams = formParams.append('username', <any>username) as any || formParams;
        }
        if (password !== undefined) {
            formParams = formParams.append('password', <any>password) as any || formParams;
        }

        return this.httpClient.post<any>(`${this.configuration.basePath}/auth/login`,
            convertFormParamsToString ? formParams.toString() : formParams,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    public loginUser(username: string, password: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public loginUser(username: string, password: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;

    /**
     * Logs the current user out and destroys the current session
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public logoutUser(observe?: 'body', reportProgress?: boolean): Observable<any>;

    public logoutUser(observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;

    public logoutUser(observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;

    public logoutUser(observe: any = 'body', reportProgress: boolean = false): Observable<any> {

        let headers = this.defaultHeaders;

        // authentication (auth) required
        if (this.configuration.username || this.configuration.password) {
            headers = headers.set('Authorization', 'Basic ' + btoa(this.configuration.username + ':' + this.configuration.password));
        }
        // to determine the Accept header
        const httpHeaderAccepts: string[] = [];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        return this.httpClient.post<any>(`${this.configuration.basePath}/auth/logout`,
            null,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Registers a new user by putting in username, email and password
     * @param registrationDto
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public registerUser(registrationDto?: RegistrationDto, observe?: 'body', reportProgress?: boolean): Observable<any>;

    public registerUser(registrationDto?: RegistrationDto, observe: any = 'body', reportProgress: boolean = false): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        const httpHeaderAccepts: string[] = [];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.post<any>(`${this.configuration.basePath}/user/registration/register`,
            registrationDto,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    public registerUser(registrationDto?: RegistrationDto, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public registerUser(registrationDto?: RegistrationDto, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;

    /**
     * Your GET endpoint
     * Resets a password based on a password reset request
     * @param passwordResetDto
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public resetPasswordRequest(passwordResetDto?: PasswordResetDto, observe?: 'body', reportProgress?: boolean): Observable<any>;

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (const consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }
    public resetPasswordRequest(passwordResetDto?: PasswordResetDto, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public resetPasswordRequest(passwordResetDto?: PasswordResetDto, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public resetPasswordRequest(passwordResetDto?: PasswordResetDto, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {

        let headers = this.defaultHeaders;

        // to determine the Accept header
        const httpHeaderAccepts: string[] = [
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }


        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.get<any>(`${this.configuration.basePath}/user/forgotPassword/resetPassword`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}
