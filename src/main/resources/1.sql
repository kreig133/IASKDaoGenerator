
--IDV 27/04/05
-------------------------------------------------- процедура создания новой кредаты (копирование с вытиранием)
CREATE PROCEDURE dbo.idv_StringUnFix (	@packid 	  int,			-- ID договора
					@CUID		  int,			-- СУИЦИД Пользователя
					@InComm 	  varchar(255) = NULL,  -- коментарии к созданию кредаты
					@teCreData_Source int	       = NULL,  -- теневая кредата (если подана - создается из teCredata)
					@CreData_Source   int	       = NULL,  -- обычная кредата (если NULL - создается из актуальной)
					@IsEdit		  smallInt     = 1,     -- 1 - создается для исправления ошибок,
										-- 0 - для регистрации переоформления,
										-- 2 - реструктуризации
					@RestType	  smallInt     = 1,	-- тип реструктуризации, 1 - по умолчанию
										-- при @IsEdit <> 2 игнорируется
					@IsWriteJrl	  smallInt     = 1,	-- 1 - Да
					@IsNtf		  smallInt     = 1,	-- 1 - Да
					@IsScenMove  	  smallInt     = 0,	-- 1 - Да
					@DocCOPY          smallInt     = 1,	-- (-1) НЕ КОПИРОВАТЬ,
										--  (0) ВСЕ,
										--  (1) ИНСПЕКТ-ЭКОН,
										--  (2) БУХГ
										AS
					@CREDATA_NEW	  int	       = 0 OUTPUT,	-- ID созданнЙ кредаты
					@RType		  smallint     = 0	-- результат:
										-- 0 - select | other - Return+OutPut
				 ) AS
-----------------------------------------------------------------------------------
-- Result: 0   - OK
--         <>0 - Error
-----------------------------------------------------------------------------------



declare @JRLSTATUS 	int
declare @JRLID 		int
declare @whowork   	smallint
declare @FType		int
declare	@rez 		int
declare	@ErrNo 		int
declare @Exc            VarChar(255)
Declare @Tit varchar(255), @Mes varchar(255), @Mes1 varchar(255), @TextV varchar(255)
Declare @ZName varchar(122), @NLog varchar(64), @UName varchar(50)
Declare @CorrLapSc int

if isnull(@RestType,0) not in (1,2,3,4,5,6) select @RestType = 1

--******************************
if isnull(@DocCOPY,-1) = -1
	select	@WHOWORK = 0
if isnull(@DocCOPY,-1) = 0
	select	@WHOWORK = 1 + 8 + 16 + 64 + 2 + 32 + 128
if isnull(@DocCOPY,-1) = 1
	select	@WHOWORK = 1 + 8 + 16 + 64 + 128
if isnull(@DocCOPY,-1) = 2
	select	@WHOWORK = 2 + 32
--******************************

select	@UName = sName1+ ' ' + substring(isnull(sName2,'  '),1,1) + '.' + substring(isnull(sName3,'  '),1,1) + '.',
	@FType = nFTypeID
from	tUsers
where	nCUID = @CUID

DECLARE @SAVEname VarChar(30) 		-- Имя точки сохранения (NULL - открыта собственная транзакция)
--
select	@CREDATA_NEW  =0, @Exc = '', @rez = 0
select  @Mes = 'Дата/Время начала: ' + CONVERT(varchar(10),getdate(),104)+' '+CONVERT(varchar(8),getdate(),108),
	@Mes1 = char(13)+CHAR(10)+'Основание:'+char(13)+CHAR(10),
	@Tit = 'По договору начата корректировка Условий & Реквизитов'
--
/*
EXECUTE @rez = dbo.merGetPackRights @packid, @Exc OUTPUT, 0
if @rez <> 0 or isnull(DATALENGTH(@Exc),0) < 3 or @@ERROR <> 0	-- нет доступа к делу
BEGIN			-- нет доступа к делу
	select @ErrNo = -2000
	GOTO ERR
END
*/
--
if isnull(@CreData_Source,0) = 0	-- с актульной строки
BEGIN
	select 	@CreData_Source = a.nCreDataID, @NLog = isnull(a.spacktitle,'б/н'),
		@ZName =   (CASE WHEN cl.nCliTypeID = 1 THEN isnull(jp.sName, '???')
				     WHEN cl.nCliTypeID = 2 THEN isnull(fp.sSurName, '???')+' '+isnull(fp.sName, '')
				     WHEN cl.nCliTypeID = 3 THEN '???'
				     WHEN cl.nCliTypeID = 4 THEN '???'
							    ELSE '???'
				END)
	from 		tCreData     a (NOLOCK)
	JOIN		tclients    cl (NOLOCK) On a.nclientid = cl.nclientid
	LEFT OUTER JOIN tjurpersons jp (NOLOCK) On (a.nrekid = jp.nrekid and cl.nCliTypeID in (1,3))
	LEFT OUTER JOIN tfispersons fp (NOLOCK) On (a.nrekid = fp.nrekid and cl.nCliTypeID = 2)
	where a.nFixed = 1 and a.nActual = 1 and a.nPackID = @PackID
END
ELSE
BEGIN
	if exists(	select 	ncredataid from tcredata (NOLOCK)
			where	ncredataid = @CreData_Source
			and	(nfixed = 0
			    or  (nfixed = 1 and dfixdate is null)
			    or  (nfixed = 1 and (dfixdate is NOT null) and isnull(nedited,0) <> 0 and isnull(nactual,0) <> 1))
		 )
	begin
		select @ErrNo = -1998	-- с этой кредаты старт корректировки запрещен
		GOTO ERR
	end
	--
	select 	@NLog = isnull(a.spacktitle,'б/н'),
		@ZName =   (CASE WHEN cl.nCliTypeID = 1 THEN isnull(jp.sName, '???')
				     WHEN cl.nCliTypeID = 2 THEN isnull(fp.sSurName, '???')+' '+isnull(fp.sName, '')
				     WHEN cl.nCliTypeID = 3 THEN '???'
				     WHEN cl.nCliTypeID = 4 THEN '???'
							    ELSE '???'
				END)
	from 		tCreData     a (NOLOCK)
	JOIN		tclients    cl (NOLOCK) On a.nclientid = cl.nclientid
	LEFT OUTER JOIN tjurpersons jp (NOLOCK) On (a.nrekid = jp.nrekid and cl.nCliTypeID in (1,3))
	LEFT OUTER JOIN tfispersons fp (NOLOCK) On (a.nrekid = fp.nrekid and cl.nCliTypeID = 2)
	where a.nCreDataID = @CreData_Source
END

if @CreData_Source = 0
begin
	select @ErrNo = -1999
	GOTO ERR
end

if (isnull(@teCreData_Source, 0) > 0) and (not exists(select ntCreDataID from tecredata where ntCreDataID = @teCreData_Source))
begin
	select @ErrNo = -2002
	GOTO ERR
end

if isnull(@teCreData_Source, 0) > 0		-- инициализация с теневой строки
BEGIN
	select 	@NLog = isnull(a.spacktitle,'б/н'),
		@ZName =   (CASE WHEN cl.nCliTypeID = 1 THEN isnull(jp.sName, '???')
				     WHEN cl.nCliTypeID = 2 THEN isnull(fp.sSurName, '???')+' '+isnull(fp.sName, '')
				     WHEN cl.nCliTypeID = 3 THEN '???'
				     WHEN cl.nCliTypeID = 4 THEN '???'
							    ELSE '???'
				END)
	from 		teCreData    a (NOLOCK)
	JOIN		tclients    cl (NOLOCK) On a.nclientid = cl.nclientid
	LEFT OUTER JOIN tjurpersons jp (NOLOCK) On (a.nrekid = jp.nrekid and cl.nCliTypeID in (1,3))
	LEFT OUTER JOIN tfispersons fp (NOLOCK) On (a.nrekid = fp.nrekid and cl.nCliTypeID = 2)
	where a.ntCreDataID = @teCreData_Source
	--
	/****** ОТКРЫВАЮ ТРАНЗАКЦИЮ ******/
	IF ISNULL(@@TRANCOUNT, 0) > 0          -- Анализирую состояние счетчика транзакций
  	BEGIN                                  -- Есть открытая(ые) транзакция(и)
    	  SELECT @SAVEname = 'idv_StringUnFix' -- Использую в качестве имени точки сохранения имя хранимой процедуры
--    	  SAVE TRANSACTION @SAVEname           -- Устанавливаю точку сохранения
  	END
	ELSE
  	BEGIN                                  -- Открытых транзакция НЕТ
    	  SELECT @SAVEname = NULL              -- заNULLиваю имя точки сохранения
    	  BEGIN TRANSACTION                    -- открываю собственную транзакцию
  	END
--...............................................
	-- копирование новой строки из теневой
	insert into tcredata (nCredataSource,
		nPackID,sPackTitle,nClientID,nRekID,dDateStart,dDateStop,sDateLen,sNewComm,
		nCurTypeID,nLimitCurTypeID,nCreAmount,nInspAmount,nCliAmount,dLimitEnd,nLimitTypeID,nRPS,
		nTypePropId, nTrBal, nPercBal, nProcAuto, nOptim,
		nCngClause,nProlong,nRestruct,dRestruct,n62a,nPeople2,
		nFtype,nOver,nNotRegularProc,nNotRegularPlat,
		nLimitSet, nMaxDolgSet, nMaxDolg, nMaxDolgCur,
		nQID,nRURFactorID,nAccaListID,nLimitKindAuto,nLimitKind,
		nReglamId,nLoanTypeId,nPurposeId,nSpecialId,nReglamDeId,nLoanTypeDeId,nPurposeDeId,nType4RNU,nDepartID,nIsAnnuity,nDepositID,
		dFirstDogSign,dClientSign,dMMYYYYStart,sCardAcca,dCardAccaStart,sAccaPack,sAccaPack_ss,nPeople,nBuro,nExistGar,nNova,nSubSidy,
		nEdited,dAttachDate,dFixDate,nFixed,nActual,nMoraSrok,nTransPlan
		)
	select  nCreDataID,
		nPackID,sPackTitle,nClientID,nRekID,dDateStart,dDateStop,sDateLen,@InComm,
		nCurTypeID,nLimitCurTypeID,nCreAmount,nInspAmount,nCliAmount,dLimitEnd,nLimitTypeID,nRPS,
		nTypePropId, nTrBal, nPercBal, nProcAuto, nOptim,
		'nCngClause' = (CASE WHEN @IsEdit = 1 THEN nCngClause ELSE NULL END),
		'nProlong'   = (CASE WHEN @IsEdit = 1 THEN nProlong ELSE NULL END),
		'nRestruct'  = (CASE WHEN @IsEdit = 1 THEN nRestruct WHEN @IsEdit = 2 THEN @RestType ELSE NULL END),
		'dRestruct'  = (CASE WHEN @IsEdit in (1,0) THEN dRestruct ELSE NULL END),
		'n62a' = (CASE WHEN @IsEdit = 1 THEN n62a ELSE NULL END),nPeople2,
		'nFtype' = (CASE WHEN @IsEdit = 1 and nFtype < 0 THEN nFtype ELSE @Ftype END) ,
		nOver,nNotRegularProc,nNotRegularPlat,
		nLimitSet, nMaxDolgSet, nMaxDolg, nMaxDolgCur,
		'IsEdt'	     = (CASE WHEN @IsEdit = 1 THEN 1 ELSE 0 END),
		nRURFactorID,nAccaListID,nLimitKindAuto,nLimitKind,
		nReglamId,nLoanTypeId,nPurposeId,nSpecialId,nReglamDeId,nLoanTypeDeId,nPurposeDeId,nType4RNU, nDepartID,nIsAnnuity,nDepositID,
		dFirstDogSign,'dClientSign' = (CASE WHEN @IsEdit = 1 THEN dClientSign ELSE NULL END),
		'dMMYYYYStart' = (CASE WHEN @IsEdit = 1 THEN dClientSign ELSE NULL END),sCardAcca,dCardAccaStart,sAccaPack,sAccaPack_ss,nPeople,nBuro,nExistGar,nNova,nSubSidy,
		NULL,'dAttachDate' = (CASE WHEN @IsEdit = 1 THEN dAttachDate ELSE NULL END),NULL,0,0,nMoraSrok,nTransPlan
	from tecredata (HOLDLOCK)
	where ntCreDataID = @teCreData_Source
	--
	select @CREDATA_NEW = @@IDENTITY
	-- проверка
	if isnull(@CREDATA_NEW, 0) = 0
	BEGIN
		IF (@SAVEname IS NULL)
       			ROLLBACK TRANSACTION
--		ELSE
--       			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1999
		GOTO ERR
	END
	ELSE
	BEGIN
	-- копирование связок на документы из теневой таблицы связи и обработка невидимых документов
	insert into tdoccdatarel (nCreDataID,nDocID,nNonSinc)
	select	@CREDATA_NEW, te.nDocId,
		'NonSinc' = 	(CASE WHEN (sd.noperwhose & @whowork <> 0) THEN NULL
					 		   		   ELSE 10
				 END)	--
/*		'NonSinc' = 	(CASE WHEN te.nDocId IN (select cd.nDocID
							 from	tDocCDataRel cd
							 where	cd.ncredataid = @CreData_Source ) THEN NULL
					 		   				ELSE 1
				 END)*/
	from 	tedoccdatarel   te
	join	tdocpack	dp On te.nDocID = dp.nDocID
	join	tfinlaps	fl On dp.nLapPackID = fl.nLapPackID
	join	tScensData	sd On fl.nScenRecID = sd.nScenRecID
	where 	te.ntCreDataID = @teCreData_Source
--	and	(sd.noperwhose & @whowork <> 0)

	-- копирование tPackList
		EXECUTE @rez = dbo.idv_TPLCopy @packid = @packid
		if @rez <> 0
		BEGIN
			IF (@SAVEname IS NULL)
       				ROLLBACK TRANSACTION
--			ELSE
--       				ROLLBACK TRANSACTION @SAVEname
			--
			select @ErrNo = -1999
			GOTO ERR
		END
	-- регистрация JRL
	if Isnull(@IsWriteJrl,0) = 1
	BEGIN
		SELECT	@JRLSTATUS = (CASE WHEN isnull(@IsEdit,1) = 1 THEN 7  ELSE 10 END),
			@JRLID     = (CASE WHEN isnull(@IsEdit,1) = 1 THEN 58
					   WHEN	isnull(@IsEdit,1) = 2 THEN 123 ELSE 57 END)
                /*
		EXECUTE @rez = dbo.merRecFiscalJRL 	@Tit,
						@packid,
						@Mes,
						@Mes1,
						@InComm,
						NULL,
						NULL,
						0,
						NULL,
						NULL,
						NULL,
						@CREDATA_NEW,
						@JRLSTATUS,
						0,
						NULL,
						NULL,
						@JRLID
                */

		execute @rez = dbo.merRecFiscalJRL
		 @OperName  = @Tit, -- Название операции
		 @PackID    = @packid,	 -- Идентификатор кредитного дела
		 @Details1  = @Mes,      -- Детали операции 1
		 @Details2  = @Mes1,	 -- Детали операции 2
		 @Details3  = @InComm,	 -- Детали операции 3
		 @Details4  = NULL,	 -- Детали операции 4
		 @Details5  = NULL, 	 -- Детали операции 5
		 @RType     = 0,	 -- Способ возврата результата (0 : RETURN, 1 : SELECT)
		 @ContID    = NULL,	 -- Идентификатор контейнера
		 @RptID	    = NULL,	 -- Идентификатор отчета
		 @RptTypeID = NULL,	 -- Идентификатор вида отчета
		 @CreDataID = @CREDATA_NEW, -- Идентификатор строки данных (0 - общая операция по к/д)
		 @State	    = @JRLSTATUS,		-- Статус записи (если запись относится к операции по к/д)
							--  1 : Общая операция по к/д								(О)
							--  2 : Операция на этапе начального ввода данных					(В)
							--  3 : Операция на этапе сопровождения							(С)
							--  4 : Начальная операция этапа бухгалтерской корректировки				(С->К)
							--  5 : Операция на этапе бухгалтерской корректировки					(К)
							-- 14 : Операция перехода с этапа бухгалтерской корректировки на этап перерасчета	(К->П)
							--  6 : Конечная операция на этапе бухгалтерской корректировки				(К->С)
							--  7 : Начальная операция этапа редактирования условий					(С->Р)
							--  8 : Операция на этапе редактирования условий					(Р)
							-- 15 : Операция перехода с этапа редактирования условий на этап перерасчета		(Р->П)
							--  9 : Конечная операция этапа редактирования условий					(Р->С)
							-- 10 : Начальная операция этапа изменения условий					(C->И)
							-- 11 : Операция на этапе изменения условий						(И)
							-- 16 : Операция перехода с этапа изменения условий на этап перерасчета			(И->П)
							-- 12 : Конечная операция этапа изменения условий					(И->C)
							-- 13 : Конечная операция этапа перерасчета						(П->С)
							-- 17 : Начальная операция этапа редактирования архива					(А->Р)
							-- 18 : Операция на этапе редактирования архива						(А)
							-- 19 : Конечная операция этапа редактирования архива					(Р->А)
		 @Cancel	= 0,	-- Признак отмены операций этапа (для статусов 6, 9, 12, 19 : 1 - аннулирование этапа)
		 @HostName	= NULL,	-- Имя компьютера пользователя
		 @NTUserName	= NULL,	-- Имя пользователя NT
		 @OperID	= @JRLID -- Идентификатор журнальной операции
		 /*
		 @ObjRecID	Int		= NULL	-- Идентификатор объекта записи
		 @ObjRecTypeID	Int		= NULL	-- Идентификатор типа объекта записи
		 */

		if @rez <> 0
		BEGIN
			IF (@SAVEname IS NULL)
       				ROLLBACK TRANSACTION
--			ELSE
--       				ROLLBACK TRANSACTION @SAVEname
			--
			select @ErrNo = -1997
			GOTO ERR
		END
	END
	END

	-- нотификация заинтересованных лиц
	-- нотификация ЭКОНОМИСТОВ
	If isnull(@IsNtf,0) = 0 GOTO NO_SEND1
	If isnull(@FType,0) not IN (19,20) GOTO NO_SEND1
	DELETE from teDolg WHERE nCUID = @CUID
	INSERT INTO teDolg(nCUID, nPackID, nCreDataID)
	VALUES (@CUID, @PackID, @CreData_Source)
	EXECUTE @rez = dbo.merGetPackOwners @CUID = @CUID, @FTypeMask = '0000000000011000', @AccessMask = '12'
	if @rez <> 0 or @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
			ROLLBACK TRANSACTION
--		ELSE
--			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1995
		GOTO ERR
	END
	--
	delete from teDolg Where nCUIDOwner IS NULL and nCUID = @CUID
	-- посылаем оповещение List
	select @TextV = 'Мною начато изменение условий и ревизитов.'
	EXECUTE dbo.idv_msgpackparm 	@CREDATAID	= @CREDATA_NEW,
				@CUID		= @CUID,
				@TextV 		= @TextV,
				@Text1 		= @Mes	OUTPUT,
				@Text2 		= @Mes1	OUTPUT,
				@Rtype		= 0
	if @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
       			ROLLBACK TRANSACTION
		--
		select @ErrNo = -2005
		GOTO Err
	END
--
	insert into 	tNotifyMsgQueue (nTo, nUoID, sMsg1, sMsg2)
	select		nCUIDOwner, 1,
			@Mes+char(13)+char(10), @Mes1 + char(13)+char(10)
	from 		teDolg
	where		nCUID = @CUID
	if @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
			ROLLBACK TRANSACTION
--		ELSE
--			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1995
		GOTO ERR
	END

	-- посылаем оповещение MessageBox
	insert into 	tNotifyMsgQueue (nTo, nUoID, sMsg1, sMsg2)
	select		nCUIDOwner, 0,
			@Mes+char(13)+char(10), @Mes1 + char(13)+char(10)
	from 		teDolg
	where		nCUID = @CUID
	if @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
			ROLLBACK TRANSACTION
--		ELSE
--			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1995
		GOTO ERR
	END
NO_SEND1:
IF (@SAVEname IS NULL) COMMIT TRANSACTION
END
ELSE					-- обычная инициализация с фиксированной строки
BEGIN
	/****** ОТКРЫВАЮ ТРАНЗАКЦИЮ ******/
	IF ISNULL(@@TRANCOUNT, 0) > 0          -- Анализирую состояние счетчика транзакций
  	BEGIN                                  -- Есть открытая(ые) транзакция(и)
    	  SELECT @SAVEname = 'idv_StringUnFix' -- Использую в качестве имени точки сохранения имя хранимой процедуры
--    	  SAVE TRANSACTION @SAVEname           -- Устанавливаю точку сохранения
  	END
	ELSE
  	BEGIN                                  -- Открытых транзакция НЕТ
    	  SELECT @SAVEname = NULL              -- заNULLиваю имя точки сохранения
    	  BEGIN TRANSACTION                    -- открываю собственную транзакцию
  	END
--...............................................
	-- копирование новой строки
	insert into tcredata (nCredataSource,
		nPackID,sPackTitle,nClientID,nRekID,dDateStart,dDateStop,sDateLen,sNewComm,
		nCurTypeID,nLimitCurTypeID,nCreAmount,nInspAmount,nCliAmount,dLimitEnd,nLimitTypeID,nRPS,
		nTypePropId, nTrBal, nPercBal, nProcAuto, nOptim,
		nCngClause,nProlong,nRestruct,dRestruct,n62a,nPeople2,
		nFtype,nOver,nNotRegularProc,nNotRegularPlat,
		nLimitSet, nMaxDolgSet, nMaxDolg, nMaxDolgCur,
		nQID,nRURFactorID,nAccaListID,nLimitKindAuto,nLimitKind,
		nReglamId,nLoanTypeId,nPurposeId,nSpecialId,nReglamDeId,nLoanTypeDeId,nPurposeDeId,nType4RNU, nDepartID,nIsAnnuity,nDepositID,
		dFirstDogSign,dClientSign,dMMYYYYStart,sCardAcca,dCardAccaStart,sAccaPack,sAccaPack_ss,nPeople,nBuro,nExistGar,nNova,nSubSidy,
		nEdited,dAttachDate,dFixDate,nFixed,nActual,nMoraSrok,nTransPlan
		)
	select  @CreData_Source,
		nPackID,sPackTitle,nClientID,nRekID,dDateStart,dDateStop,sDateLen,@InComm,
		nCurTypeID,nLimitCurTypeID,nCreAmount,nInspAmount,nCliAmount,dLimitEnd,nLimitTypeID,nRPS,
		nTypePropId, nTrBal, nPercBal, nProcAuto, nOptim,
		'nCngClause' = (CASE WHEN @IsEdit = 1 THEN nCngClause ELSE NULL END),
		'nProlong'   = (CASE WHEN @IsEdit = 1 THEN nProlong ELSE NULL END),
		'nRestruct'  = (CASE WHEN @IsEdit = 1 THEN nRestruct WHEN @IsEdit = 2 THEN @RestType ELSE NULL END),
		'dRestruct'  = (CASE WHEN @IsEdit in (1,0) THEN dRestruct ELSE NULL END),
		'n62a' = (CASE WHEN @IsEdit = 1 THEN n62a ELSE NULL END),nPeople2,
		'nFtype' = (CASE WHEN @IsEdit = 1 and nFtype < 0 THEN nFtype ELSE @Ftype END) ,
		nOver,nNotRegularProc,nNotRegularPlat,
		nLimitSet, nMaxDolgSet, nMaxDolg, nMaxDolgCur,
		'IsEdt'	     = (CASE WHEN @IsEdit = 1 THEN 1 ELSE 0 END),
		nRURFactorID,nAccaListID,nLimitKindAuto,nLimitKind,
		nReglamId,nLoanTypeId,nPurposeId,nSpecialId,nReglamDeId,nLoanTypeDeId,nPurposeDeId,nType4RNU, nDepartID,nIsAnnuity,nDepositID,
		dFirstDogSign,'dClientSign' = (CASE WHEN @IsEdit = 1 THEN dClientSign ELSE NULL END),
		'dMMYYYYStart' = (CASE WHEN @IsEdit = 1 THEN dClientSign ELSE NULL END),sCardAcca,dCardAccaStart,sAccaPack,sAccaPack_ss,nPeople,nBuro,nExistGar,nNova,nSubSidy,
		NULL,'dAttachDate' = (CASE WHEN @IsEdit = 1 THEN dAttachDate ELSE NULL END),NULL,0,0,nMoraSrok,nTransPlan
	from tcredata (HOLDLOCK)
	where nCreDataID = @CreData_Source
	--
	select @CREDATA_NEW = @@IDENTITY
	-- проверка
	if isnull(@CREDATA_NEW, 0) = 0
	BEGIN
		IF (@SAVEname IS NULL)
       			ROLLBACK TRANSACTION
--		ELSE
--       			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1999
		GOTO ERR
	END
	ELSE
	BEGIN
	-- копирование связок на документы
	insert into tdoccdatarel (nCreDataID,nDocID,nNonSinc)
	select	@CREDATA_NEW, cd.nDocId,
		'NonSinc' = 	(CASE WHEN (sd.noperwhose & @whowork <> 0) THEN NULL
					 		   		   ELSE 10
				 END)	--
	from 	tdoccdatarel	cd
	join	tdocpack	dp On cd.nDocID = dp.nDocID
	join	tfinlaps	fl On dp.nLapPackID = fl.nLapPackID
	join	tScensData	sd On fl.nScenRecID = sd.nScenRecID
	where 	cd.nCreDataID = @CreData_Source
--	and	(sd.noperwhose & @whowork <> 0)

	-- копирование tPackList
		EXECUTE @rez = dbo.idv_TPLCopy @packid = @packid
		if @rez <> 0
		BEGIN
			IF (@SAVEname IS NULL)
       				ROLLBACK TRANSACTION
--			ELSE
--       				ROLLBACK TRANSACTION @SAVEname
			--
			select @ErrNo = -1999
			GOTO ERR
		END
	-- регистрация JRL
	if Isnull(@IsWriteJrl,0) = 1
	BEGIN
		if exists(select nPackID from tCreData (NOLOCK) Where nCreDataID = @CreData_Source and nactual = 1)
			SELECT	@JRLSTATUS = (CASE WHEN isnull(@IsEdit,1) = 1 THEN 7  ELSE 10 END),
				@JRLID     = (CASE WHEN isnull(@IsEdit,1) = 1 THEN 58
						   WHEN	isnull(@IsEdit,1) = 2 THEN 123 ELSE 57 END)
		else
			SELECT	@JRLSTATUS = 17,
				@JRLID = 58
		--
		/*
		EXECUTE @rez = dbo.merRecFiscalJRL 	@Tit,
						@packid,
						@Mes,
						@Mes1,
						@InComm,
						NULL,
						NULL,
						0,
						NULL,
						NULL,
						NULL,
						@CREDATA_NEW,
						@JRLSTATUS,
						0,
						NULL,
						NULL,
						@JRLID
						*/

		execute @rez = dbo.merRecFiscalJRL
		 @OperName  = @Tit, -- Название операции
		 @PackID    = @packid,	 -- Идентификатор кредитного дела
		 @Details1  = @Mes,      -- Детали операции 1
		 @Details2  = @Mes1,	 -- Детали операции 2
		 @Details3  = @InComm,	 -- Детали операции 3
		 @Details4  = NULL,	 -- Детали операции 4
		 @Details5  = NULL, 	 -- Детали операции 5
		 @RType     = 0,	 -- Способ возврата результата (0 : RETURN, 1 : SELECT)
		 @ContID    = NULL,	 -- Идентификатор контейнера
		 @RptID	    = NULL,	 -- Идентификатор отчета
		 @RptTypeID = NULL,	 -- Идентификатор вида отчета
		 @CreDataID = @CREDATA_NEW, -- Идентификатор строки данных (0 - общая операция по к/д)
		 @State	    = @JRLSTATUS,		-- Статус записи (если запись относится к операции по к/д)
							--  1 : Общая операция по к/д								(О)
							--  2 : Операция на этапе начального ввода данных					(В)
							--  3 : Операция на этапе сопровождения							(С)
							--  4 : Начальная операция этапа бухгалтерской корректировки				(С->К)
							--  5 : Операция на этапе бухгалтерской корректировки					(К)
							-- 14 : Операция перехода с этапа бухгалтерской корректировки на этап перерасчета	(К->П)
							--  6 : Конечная операция на этапе бухгалтерской корректировки				(К->С)
							--  7 : Начальная операция этапа редактирования условий					(С->Р)
							--  8 : Операция на этапе редактирования условий					(Р)
							-- 15 : Операция перехода с этапа редактирования условий на этап перерасчета		(Р->П)
							--  9 : Конечная операция этапа редактирования условий					(Р->С)
							-- 10 : Начальная операция этапа изменения условий					(C->И)
							-- 11 : Операция на этапе изменения условий						(И)
							-- 16 : Операция перехода с этапа изменения условий на этап перерасчета			(И->П)
							-- 12 : Конечная операция этапа изменения условий					(И->C)
							-- 13 : Конечная операция этапа перерасчета						(П->С)
							-- 17 : Начальная операция этапа редактирования архива					(А->Р)
							-- 18 : Операция на этапе редактирования архива						(А)
							-- 19 : Конечная операция этапа редактирования архива					(Р->А)
		 @Cancel	= 0,	-- Признак отмены операций этапа (для статусов 6, 9, 12, 19 : 1 - аннулирование этапа)
		 @HostName	= NULL,	-- Имя компьютера пользователя
		 @NTUserName	= NULL,	-- Имя пользователя NT
		 @OperID	= @JRLID -- Идентификатор журнальной операции
		 /*
		 @ObjRecID	Int		= NULL	-- Идентификатор объекта записи
		 @ObjRecTypeID	Int		= NULL	-- Идентификатор типа объекта записи
		 */

		if @rez <> 0
		BEGIN
			IF (@SAVEname IS NULL)
       				ROLLBACK TRANSACTION
--			ELSE
--       				ROLLBACK TRANSACTION @SAVEname
			--
			select @ErrNo = -1997
			GOTO ERR
		END
	END
	END
	-- нотификация заинтересованных лиц
	-- нотификация ЭКОНОМИСТОВ
	If isnull(@IsNtf,0) = 0 GOTO NO_SEND2
	If isnull(@FType,0) not IN (19,20) GOTO NO_SEND2
	DELETE from teDolg WHERE nCUID = @CUID
	INSERT INTO teDolg(nCUID, nPackID, nCreDataID)
	VALUES (@CUID, @PackID, @CreData_Source)
	EXECUTE @rez = dbo.merGetPackOwners @CUID = @CUID, @FTypeMask = '0000000000011000', @AccessMask = '12'
	if @rez <> 0 or @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
			ROLLBACK TRANSACTION
--		ELSE
--			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1995
		GOTO ERR
	END
	--
	delete from teDolg Where nCUIDOwner IS NULL and nCUID = @CUID
	-- посылаем оповещение List
	select @TextV = 'Мною начато изменение условий и ревизитов.'
	EXECUTE dbo.idv_msgpackparm 	@CREDATAID	= @CREDATA_NEW,
				@CUID		= @CUID,
				@TextV 		= @TextV,
				@Text1 		= @Mes	OUTPUT,
				@Text2 		= @Mes1	OUTPUT,
				@Rtype		= 0
	if @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
       			ROLLBACK TRANSACTION
		--
		select @ErrNo = -2006
		GOTO Err
	END
--
	insert into 	tNotifyMsgQueue (nTo, nUoID, sMsg1, sMsg2)
	select		nCUIDOwner, 1,
			@Mes+char(13)+char(10), @Mes1 + char(13)+char(10)
	from 		teDolg
	where		nCUID = @CUID
	if @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
			ROLLBACK TRANSACTION
--		ELSE
--			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1995
		GOTO ERR
	END

	-- посылаем оповещение MessageBox
	insert into 	tNotifyMsgQueue (nTo, nUoID, sMsg1, sMsg2)
	select		nCUIDOwner, 0,
			@Mes+char(13)+char(10), @Mes1 + char(13)+char(10)
	from 		teDolg
	where		nCUID = @CUID
	if @@ERROR <> 0
	BEGIN
		IF (@SAVEname IS NULL)
			ROLLBACK TRANSACTION
--		ELSE
--			ROLLBACK TRANSACTION @SAVEname
		--
		select @ErrNo = -1995
		GOTO ERR
	END
NO_SEND2:
IF (@SAVEname IS NULL) COMMIT TRANSACTION
END
--
IF @IsScenMove = 1
BEGIN
	select	@CorrLapSc = MIN(SC.nLapScID)
	from	tPackList PL (NOLOCK)
	join	tScens    SC (NOLOCK) On PL.nCrePlanID = SC.nCrePlanID and
					 SC.nLapID = (CASE WHEN @IsEdit = 1 THEN 12
						           WHEN @IsEdit = 0 THEN 4
							   WHEN @IsEdit = 2 THEN 15
							   ELSE 12 END)
	Where	PL.nPackID = @PackID
	and	SC.nActual = 1
	--
	if @CorrLapSc > 0
		UPDATE	tPackList
		SET	nLapSlave = @CorrLapSc
		Where	nPackID = @PackID
		and	nLapSlave IS NULL
END

if isnull(@Rtype,0) = 0 select 0
return 0

ERR:

if isnull(@Rtype,0) = 0 select @ErrNo
return @ErrNo
