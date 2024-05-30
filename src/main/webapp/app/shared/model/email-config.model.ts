export interface IEmailConfig {
  id?: number;
  emailId?: string | null;
  tokenString?: string | null;
}

export const defaultValue: Readonly<IEmailConfig> = {};
