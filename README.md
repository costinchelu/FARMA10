## FARMA 10


Application used for pharmaceutical products storage management (including receiving and selling products).  

Technologies:  
    - Java with JavaFX for interface  
    - Oracle Database 11g for persistence
    
- Application has autentification with administrator account for User management. Passwords are stored in the database as encrypted BLOB.
- logging in external text file
- IN - OUT invoice management implementing all necessary operations in a normal working environment with persistence in the database
- validations
- different lists managing items specific to the pharmaceutical domain. All item operations are detailed in the graphical interface
- invoice export in pdf using itextpdf library for Java
- list reports for invoices including graphical and pdf exports

**Screenshots:**

![Database schema](screenshots/0_db_schema.png)

![Autentification](screenshots/1_autentification.png)

![Main menu](screenshots/2_main_menu.png)

![Incoming invoice](screenshots/3_incoming_invoice.png)

![INvoice product adding](screenshots/4_in_adding_product.png)

![Confirmation dialogs](screenshots/5_alerts.png)

![PDF export for invoice](screenshots/6_pdf_invoice_export.png)

![Exit invoice](screenshots/7_exit_invoice.png)

![Adding product in exit invoice (with validation)](screenshots/8_adding_product_validation.png)

![Invoice list](screenshots/9_invoice_list.png)

![Graphical operations report](screenshots/10_operations_report.png)

![Stock reports](screenshots/11_stock_reports.png)

![Current stock listing](screenshots/12_current_stock.png)

![Current stock pdf export](screenshots/13_export_stock_to_pdf.png)

![Product register](screenshots/14_products_list.png)

![Editing entry in the product register](screenshots/15_entry_edit.png)

![Adding new product to  register](screenshots/16_adding_new_product_to_register.png)

![IUPAC name register](screenshots/17_drug_iupac_name_register.png)

![Partners register](screenshots/18_partner_register.png)

