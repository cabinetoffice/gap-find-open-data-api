INSERT INTO public.grant_submission(id,application_name,created,definition,last_updated,status,submitted_date, version,applicant_id,application_id, created_by,last_updated_by,scheme_id,gap_id,last_required_checks_export) VALUES ('3a6cfe2d-bf58-440d-9e07-3579c7dcf150', 'Application with File Upload', '2022-08-02 20:10:20', '{
  "sections": [
    {
      "sectionId": "ELIGIBILITY",
      "sectionTitle": "Eligibility",
      "sectionStatus": "COMPLETED",
      "questions": [
        {
          "questionId": "ELIGIBILITY",
          "fieldTitle": "Eligitiblity Statement",
          "displayText": "Some admin supplied text describing what it means to be eligible to apply for this grant",
          "questionSuffix": "Does your organisation meet the eligibility criteria?",
          "responseType": "YesNo",
          "validation": { "mandatory": true },
          "response": "Yes"
        }
      ]
    },
    {
      "sectionId": "ESSENTIAL",
      "sectionTitle": "Essential Information",
      "sectionStatus": "COMPLETED",
      "questions": [
        {
          "questionId": "APPLICANT_TYPE",
          "profileField": "ORG_TYPE",
          "fieldTitle": "Choose your organisation type",
          "hintText": "Choose the option that best describes your organisation",
          "responseType": "Dropdown",
          "validation": { "mandatory": true },
          "options": [
            "Limited company",
            "Non-limited company",
            "Registered charity",
            "Unregistered charity",
            "Other"
          ],
          "response": "Limited company"
        },
        {
          "questionId": "APPLICANT_ORG_NAME",
          "profileField": "ORG_NAME",
          "fieldTitle": "Enter the name of your organisation",
          "hintText": "This is the official name of your organisation. It could be the name that is registered with Companies House or the Charities Commission",
          "responseType": "ShortAnswer",
          "validation": { "mandatory": true, "minLength": 5, "maxLength": 100 },
          "response": "Some company name"
        },
         {
                  "questionId": "APPLICANT_AMOUNT",
                  "profileField": "ORG_AMOUNT",
                  "fieldTitle": "Enter the money you would wish to receive",
                  "hintText": "This is the official name of your organisation. It could be the name that is registered with Companies House or the Charities Commission",
                  "responseType": "ShortAnswer",
                  "validation": { "mandatory": true, "minLength": 5, "maxLength": 100 },
                  "response": "500"
                },
        {
          "questionId": "APPLICANT_ORG_ADDRESS",
          "profileField": "ORG_ADDRESS",
          "fieldTitle": "Enter your organisation''s address",
          "responseType": "AddressInput",
          "validation": { "mandatory": true },
          "multiResponse": [
            "9-10 St Andrew Square",
            "",
            "Edinburgh",
            "",
            "EH2 2AF"
          ]
        },
        {
          "questionId": "APPLICANT_ORG_COMPANIES_HOUSE",
          "profileField": "ORG_COMPANIES_HOUSE",
          "fieldTitle": "Please supply the Companies House number for your organisation - if applicable",
          "hintText": "Funding organisation might use this to identify your organisation when you apply for a grant. It might also be used to check your organisation is legitimate.",
          "responseType": "ShortAnswer",
          "validation": { "mandatory": true },
          "response": "98765432"
        },
        {
          "questionId": "APPLICANT_ORG_CHARITY_NUMBER",
          "profileField": "ORG_CHARITY_COMMISSION_NUMBER",
          "fieldTitle": "Please supply the Charity Commission number for your organisation - if applicable",
          "hintText": "Funding organisation might use this to identify your organisation when you apply for a grant. It might also be used to check your organisation is legitimate.",
          "responseType": "ShortAnswer",
          "validation": { "mandatory": true },
          "response": "12738494"
        },
        {
          "questionId": "BENEFITIARY_LOCATION",
          "fieldTitle": "Where will this funding be spent?",
          "hintText": "Select the location where the grant funding will be spent. You can choose more than one, if it is being spent in more than one location.\n\nSelect all that apply:",
          "adminSummary": "where the funding will be spent",
          "responseType": "MultipleSelection",
          "validation": {
            "mandatory": true
          },
          "options": [
            "North East England",
            "North West England",
            "South East England",
            "South West England",
            "Midlands",
            "Scotland",
            "Wales",
            "Northern Ireland"
          ],
          "multiResponse": ["Scotland", "North East England"]
        }
      ]
    },
    {
      "sectionId": "CUSTOM_SECTION_1",
      "sectionTitle": "Project Information",
      "questions": [
        {
          "questionId": "CUSTOM_APPLICANT_TYPE",
          "profileField": "ORG_TYPE",
          "fieldTitle": "Choose your organisation type",
          "hintText": "Choose the option that best describes your organisation",
          "responseType": "Dropdown",
          "validation": { "mandatory": false },
          "options": [
            "Limited company",
            "Non-limited company",
            "Registered charity",
            "Unregistered charity",
            "Other"
          ]
        },
        {
          "questionId": "CUSTOM_CUSTOM_QUESTION_1",
          "fieldTitle": "Description of the project, please include information regarding public accessibility (see GOV.UK guidance for a definition of public access) to the newly planted trees",
          "responseType": "LongAnswer",
          "validation": {
            "mandatory": false,
            "minLength": 100,
            "maxLength": 2000,
            "minWords": 20,
            "maxWords": 500
          }
        },
        {
          "questionId": "CUSTOM_APPLICANT_ORG_NAME",
          "profileField": "ORG_NAME",
          "fieldTitle": "Enter the name of your organisation",
          "hintText": "This is the official name of your organisation. It could be the name that is registered with Companies House or the Charities Commission",
          "responseType": "ShortAnswer",
          "validation": { "mandatory": false, "minLength": 5, "maxLength": 100 }
        },
        {
          "questionId": "CUSTOM_APPLICANT_ORG_ADDRESS",
          "profileField": "ORG_ADDRESS",
          "fieldTitle": "Enter your organisation''s address",
          "responseType": "AddressInput",
          "validation": { "mandatory": false }
        },
        {
          "questionId": "CUSTOM_APPLICANT_ORG_COMPANIES_HOUSE",
          "profileField": "ORG_COMPANIES_HOUSE",
          "fieldTitle": "Does your organisation have a Companies House number?",
          "hintText": "Funding organisation might use this to identify your organisation when you apply for a grant. It might also be used to check your organisation is legitimate.",
          "responseType": "YesNo",
          "validation": { "mandatory": false }
        },
        {
         "attachmentId": "fd7fe160-6661-40f2-b8c8-784bdec0c84b",
              "questionId": "28a96a4c-f2c1-4c20-8097-576ffa949630",
              "profileField": null,
              "fieldTitle": "Attach a file",
              "displayText": null,
              "hintText": "Please upload a file",
              "questionSuffix": null,
              "fieldPrefix": null,
              "adminSummary": null,
              "responseType": "SingleFileUpload",
              "validation": {
                "mandatory": false,
                "minLength": null,
                "maxLength": null,
                "minWords": null,
                "maxWords": null,
                "greaterThanZero": null,
                "validInput": null,
                "maxFileSizeMB": 300,
                "allowedTypes": ["DOC", "TXT", "ODT", "PDF", "XLS", "XLSX", "ZIP"]
              },
              "options": null,
              "response": "hello-world.txt",
              "multiResponse": null
            },
        {
          "questionId": "CUSTOM_CUSTOM_QUESTION_4",
          "fieldTitle": "Please provide the date of your last awarded grant",
          "responseType": "Date",
          "validation": { "mandatory": false }
        }
      ]
    }
  ]
}', '2022-08-02 20:10:20', 'SUBMITTED', '2022-09-27 12:34:56', 1, 1, 1, 1, 1, 1, 'GAP-LL-20220927-1','2022-08-03 11:48:02.669144');