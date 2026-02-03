export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  nome: string;
  cognome: string;
  email: string;
  username: string;
  password: string;
  telefono?: string;
}

export interface AuthResponse {
  token: string;
  utente: {
    idUtente: number;
    nome: string;
    cognome: string;
    email: string;
    username: string;
    telefono?: string;
    attivo: boolean;
  };
}
