#insert into dimensions (id,height,weight,width,depth) values ('S',12.5,15,10,50);
#insert into dimensions (id,height,weight,width,depth) values ('M',20,24,10,50);
#insert into dimensions (id,height,weight,width,depth) values ('L',30.5,33,10,50);


#insert into status (status_label) values ('CREATED');
#insert into status (status_label) values ('PAYDED');
#insert into status (status_label) values ('SENDER_LOCKER');
#insert into status (status_label) values ('SENDER_COURIER');
#insert into status (status_label) values ('CTR_HUB');
#insert into status (status_label) values ('RECEIVER_COURIER');
#insert into status (status_label) values ('RECEIVER_LOCKER');
#insert into status (status_label) values ('DELIVERED');
#insert into status (status_label) values ('OVER_WEIGHT');
#insert into status (status_label) values ('RETURNED');

#insert into parcel_detail (id,dimensions_id,price) values (1,'S',170);
#insert into parcel_detail (id,dimensions_id,price) values (2,'M',300);
#insert into parcel_detail (id,dimensions_id,price) values (3,'L',500);