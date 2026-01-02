import http from "./http";

export const fetchTerms = () => http.get<string[]>("/terms");
