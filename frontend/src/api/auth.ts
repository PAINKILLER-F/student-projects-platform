import UserProfile from "../model/UserProfile";
import GenericResponse from "../model/dto/GenericResponse";
import {Login, LoginState} from "../store/state/LoginState";
import {StorageKeys} from "../utils/StorageKeys";

/**
 * @return current username
 */
export function getCurrentUser() {
    if (sessionStorage.getItem(StorageKeys.AccessToken))
        return fetch('/api/auth/currentuser', {
            headers: {
                "Authorization": "Bearer " + sessionStorage.getItem(StorageKeys.AccessToken)
            }
        }).then(r => {
            if (!r.ok) {
                return r.json().then(m => {
                    throw new Error(`Not authorized ${m.message}`);
                });
            }
            return r.json();
        });
}

/**
 * @return current user profile
 */
export function getCurrentUserProfile(): Promise<GenericResponse<UserProfile>> {
    return fetch('/api/auth/userprofile', {
        headers: {
            "Authorization": "Bearer " + sessionStorage.getItem(StorageKeys.AccessToken)
        }
    }).then(r => r.json()).catch(alert);//TODO: civil & universal error handling
}

/**
 * @return current username
 */
export function login(login: string, password: string) {
    // console.log(user);
    return fetch('/api/auth/signin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({login, password})
    }).then(r => {
        switch (r.status) {
            case 401:
            case 403:
                return r.json().then(m => {
                    throw new Error(`Введены невенрные данные`);
                });
            case 400:
            case 404:
            case 500:
                return r.json().then(m => {
                    throw new Error(`Ошибка авторизации: ${m.message}`);
                });
            default:
                return r.json();
        }
    }).then((r: GenericResponse<Login>) => {
        return r.data;
    });//.catch(r => (alert(r), null));
    // return new Promise<GenericResponse<LoginState>>((res, rej) => res("vovan")); //TODO: implement
}

/**
 * @return nothing
 */
// export function logout() {
//     return new Promise<string>((res, rej) => res('')); //TODO: implement
// }

export function refreshToken() {
    return new Promise<string>((res, rej) => res('')); //TODO: implement
}

/**
 * @return current username
 */
export function register(user: UserProfile, password: string) {
    console.log(user);
    return fetch('/api/auth/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            login: user.username,
            name: user.name,
            surname: user.surname,
            group: user.group,
            interests: user.comment,
            email: user.email,
            roles: user.roles,
            skills: user.skills,
            password
        })
    }).then(r => {
        if (r.ok) {
            return r.json();
        } else {
            // const obj = (r.json() as any);
            throw "Error auth";
        }
    }).then((r: GenericResponse<Login>) => {
        return r.success ? (alert(r.message), null) : r.data;
    }).catch(r => (alert(r), null));//new Promise<string>((res, rej) => res("vovan")); //TODO: implement
}

/**
 * @return current username
 */
export function update(user: UserProfile, password?: string, newPassword?: string) {
    console.log(user);
    return new Promise<string>((res, rej) => res("vovan")); //TODO: implement
}
