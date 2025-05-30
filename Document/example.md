``` json

{
  "consentId": "consent-12345",
  "referenceId": "ref-001",
  "consentType": "ACCOUNT_CHECK",
  "externalUserId": "user-001",
  "country": "US",
  "locale": "en-US",
  "createdAt": "2025-05-29T14:23:45Z",
  "expiresAt": "2025-06-29T14:23:45Z",

  "status": {
    "consentStatus": "GRANTED",
    "linkStatus": "COMPLETED"
  },

  "originalRequest": {
    "referenceId": "ref-001",
    "externalUserId": "user-001",
    "externalUserAlais": "user-001",
    "consentType": "ACCOUNT_CHECK",
    "country": "US",
    "locale": "en-US",
    "redirectUrl": "https://example.com/redirect",
  },

    "accounts": [
        {
          "financialInstitution": {
            "name": "Deutsche Bank",
            "bic": "DABASESX",
            "logoUrl": "https://example.com/logos/deutsche.png"
          },
          "accountId": "f86dcb84cc164f41a166e3f56e019303",
          "accountType": "SAVINGS",
          "currencyCode": "EUR",
          "accountName": "Michael's Account",
          "segment": "PERSONAL",
          "status": "ACTIVE",
          "accountIdentifiers": [
            {
              "type": "IBAN",
              "value": "SE7921000813610123456789"
            }
          ],
          "accountHolders": [
            {
              "name": "Michael, Andreas",
              "role": "PRIMARY",
              "dateOfBirth": "1985-08-24",
              "addresses": [
                {
                  "type": "RESIDENTIAL",
                  "addressLines": ["Park Str. 68"],
                  "city": "Solingen Höhscheid",
                  "state": "Nordrhein-Westfalen",
                  "postalCode": "42699",
                  "country": "DE"
                }
              ],
              "emails": [
                {
                  "address": "Michael.Ands@hotmail.com",
                  "type": "PRIMARY"
                }
              ],
              "phoneNumbers": [
                {
                  "number": "+49-08708910130",
                  "type": "MOBILE"
                }
              ]
            }
          ]
        }
      ]
    }
```


``` json
{
  "consentId": "consent-67890",
  "consentType": "Payment",
  "consentStatus": "INITIATED",
  "linkStatus": "PENDING_VERIFICATION",
  "accountStatus": "PENDING",
  "createdAt": "2025-05-29T10:00:00Z",
  "expiresAt": "2025-06-29T10:00:00Z",
  "externalUserId": "user-002",
  "country": "US",
  "locale": "en-US",
  "referenceId": "ref-002",
  "originalRequest": {
    "referenceId": "ref-001",
    "externalUserId": "user-001",
    "externalUserAlais": "user-001",
    "consentType": "ACCOUNT_CHECK",
    "country": "US",
    "locale": "en-US",
    "redirectUrl": "https://example.com/redirect",
  },  
  "accounts": []
  "microdepositDetails": {
      "sentAt": "2025-05-29T10:05:00Z",
      "estimatedVerificationDeadline": "2025-06-01T10:05:00Z"
  }
}


```


```json
{
  "consentId": "consent-98765",
  "consentType": "Payment",
  "consentStatus": "FAILED",
  "linkStatus": "FAILED",
  "accountStatus": "NO_ACCOUNTS_LINKED",
  "failureReason": "USER_ABANDONED",
  "createdAt": "2025-05-29T09:00:00Z",
  "expiresAt": "2025-06-29T09:00:00Z",
  "externalUserId": "user-003",
  "country": "US",
  "locale": "en-US",
  "referenceId": "ref-003",
  "accounts": []
}

```

Possible Values for failureReason (LINK_FAILED):
•	IFRAME_CLOSED
•	OAUTH_TIMEOUT
•	USER_ABANDONED
•	BANK_ERROR
•	MICRODEPOSIT_TRANSACTION_FAILED
•	SESSION_EXPIRED

```json

{
  "consentId": "consent-65432",
  "consentType": "Payment",
  "consentStatus": "FAILED",
  "linkStatus": "FAILED",
  "accountStatus": "NO_ACCOUNTS_LINKED",
  "failureReason": "OAUTH_FAILED",
  "createdAt": "2025-05-29T08:00:00Z",
  "expiresAt": "2025-06-29T08:00:00Z",
  "externalUserId": "user-004",
  "country": "US",
  "locale": "en-US",
  "referenceId": "ref-004",
  "accounts": []
}

```

🔖 Possible Values for failureReason (CONSENT_FAILED):
•	OAUTH_FAILED
•	MICRODEPOSIT_VERIFICATION_FAILED
•	USER_ABANDONED
•	BANK_ERROR
•	SESSION_EXPIRED
•	INVALID_INPUT
•	PROVIDER_UNAVAILABLE



linkStatus: INITIATED, OPEN, COMPLETED, CANCELLED, FAILED, EXPIRED
consentStatus: INITIATED, GRANTED, REVOKED, FAILED, EXPIRED
accountStatus: ACTIVE, INACTIVE, NO_ACCOUNTS_LINKED



