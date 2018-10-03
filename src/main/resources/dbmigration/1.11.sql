-- apply changes
create table rc_trade_sales_log (
  id                            integer auto_increment not null,
  uuid                          varchar(40),
  sold_item_id                  integer not null,
  item                          varchar(255),
  amount                        integer not null,
  price                         double not null,
  action                        varchar(255),
  timestamp                     datetime(6),
  constraint pk_rc_trade_sales_log primary key (id)
);

create table rc_trade_sales (
  id                            integer auto_increment not null,
  storage_id                    integer not null,
  player                        varchar(255),
  player_id                     varchar(40),
  date                          datetime(6),
  world                         varchar(255),
  world_id                      varchar(40),
  constraint pk_rc_trade_sales primary key (id)
);

create table rc_trade_tradeset_cache (
  id                            integer auto_increment not null,
  trade_set                     varchar(255),
  last_update                   datetime(6),
  constraint pk_rc_trade_tradeset_cache primary key (id)
);

create table rc_trade_tradeset_cache_items (
  id                            integer auto_increment not null,
  cache_id                      integer,
  item                          varchar(255),
  amount                        integer not null,
  price                         double not null,
  constraint pk_rc_trade_tradeset_cache_items primary key (id)
);

create index ix_rc_trade_tradeset_cache_items_cache_id on rc_trade_tradeset_cache_items (cache_id);
alter table rc_trade_tradeset_cache_items add constraint fk_rc_trade_tradeset_cache_items_cache_id foreign key (cache_id) references rc_trade_tradeset_cache (id) on delete restrict on update restrict;

