<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://www.paymentresources.com/webservices/" xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" targetNamespace="http://www.paymentresources.com/webservices/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;font size='4'&gt;&lt;a href='http://www.paymentresource.com'&gt;Payment Resources International&lt;/a&gt;&lt;/font&gt; &lt;font size='3'&gt;provides this Credit Card Processing Web Service for its customers. &lt;BR&gt;In order to process a credit card you will need to have a valid Merchant Account with us. &lt;BR&gt;However, two methods below can be tested without supplying a MerchantID nor RegKey.&lt;BR&gt;WebMethods CreditCardSale,CreditCardCredit, and VoidCreditCardSale will return sample results if you leave MerchantID and RegKey blank(you will need to fill in the other fields). CreditCardSale will return a fictitious authorization if the amount is even(ie 2.00,1.02). Otherwise it will return a decline. &lt;BR&gt; For more information on this service go to &lt;a href='CreditCardProcessing.htm'&gt;Processing Credit Cards&lt;/a&gt;&lt;/font&gt;</wsdl:documentation>
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://www.paymentresources.com/webservices/">
      <s:element name="CloseBatch">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="ForceSettlement" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CreditID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CloseBatchResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CloseBatchResult" type="tns:BankCardSettleStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="BankCardSettleStatus">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:simpleType name="StatusCode">
        <s:restriction base="s:string">
          <s:enumeration value="Settled" />
          <s:enumeration value="Authorized" />
          <s:enumeration value="Declined" />
          <s:enumeration value="Voided" />
          <s:enumeration value="Canceled" />
          <s:enumeration value="Qued" />
          <s:enumeration value="UnKnown" />
          <s:enumeration value="NotAuthorized" />
          <s:enumeration value="Denied" />
          <s:enumeration value="Approved" />
        </s:restriction>
      </s:simpleType>
      <s:element name="ReleaseBatch">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="ReleaseBatchResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="ReleaseBatchResult" type="tns:ReleaseBatchSettleStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ReleaseBatchSettleStatus">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="BatchNo" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="NoOfCreditTransaction" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="NoOfSalesTransaction" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SalesAmount" type="s:decimal" />
          <s:element minOccurs="1" maxOccurs="1" name="CreditAmount" type="s:decimal" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CreditCardAutoSettle">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardAutoSettleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardAutoSettleResult" type="tns:BankCardSettleStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="VerifyAccount">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PaymentDesc" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchZIP" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchCustPNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MCC" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSConditionCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EComInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="AuthCharInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardCertData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CAVVData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="VerifyAccountResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="VerifyAccountResult" type="tns:AccountVerificationResponse" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="AccountVerificationResponse">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CVV2Code" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ACI" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthSource" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="TransactionIdentifier" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ValidationCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CAVVResultCode" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CreditCardSale">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardSaleResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="BankCardDebitStatus">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CVV2Code" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="DebitCardSale">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EncryptedPIN" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SMID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PINFormat" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CashbackAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="VoucherNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DebitCardSaleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="DebitCardSaleResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DebitCardSaleTransit">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EncryptedPIN" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SMID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PINFormat" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CashbackAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="VoucherNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransitTypeIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransitModeIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DebitCardSaleTransitResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="DebitCardSaleTransitResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleUser">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="sUserID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleUserResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardSaleUserResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardVoiceAuthorization">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="VoiceAuthCode" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardVoiceAuthorizationResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardVoiceAuthorizationResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardVoiceAuthorizationUser">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="VoiceAuthCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="sUserID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardVoiceAuthorizationUserResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardVoiceAuthorizationUserResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="BlindCredit">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="BlindCreditResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="BlindCreditResult" type="tns:BankCardCreditStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="BankCardCreditStatus">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="CreditID" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CreditCardCredit">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardCreditResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardCreditResult" type="tns:BankCardCreditStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DebitCardCredit">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="DebitCardCreditResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="DebitCardCreditResult" type="tns:BankCardCreditStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="VoidCreditCardSale">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="VoidCreditCardSaleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="VoidCreditCardSaleResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="VoidDebitCardSale">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="VoidDebitCardSaleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="VoidDebitCardSaleResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCVoidSaleSuspectedFraud">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SuspectFraud" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCVoidSaleSuspectedFraudResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CCVoidSaleSuspectedFraudResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetBankCardDebitStatus">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetBankCardDebitStatusResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetBankCardDebitStatusResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetBankCardCreditStatus">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetBankCardCreditStatusResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetBankCardCreditStatusResult" type="tns:BankCardCreditStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleAll">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef1" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef3" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef4" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleAllResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardSaleAllResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleAllTransit">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransitTypeIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransitModeIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef1" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef3" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef4" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleAllTransitResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardSaleAllTransitResult" type="tns:BankCardDebitStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleAllRSP">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef1" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef3" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UsrDef4" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardSaleAllRSPResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardSaleAllRSPResult" type="tns:BankCardDebitStatusRsp" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="BankCardDebitStatusRsp">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="Responsecode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CVV2Code" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="GetTransactionDetails">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PostedDate" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="GetTransactionDetailsResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="GetTransactionDetailsResult" type="tns:TransDetailResponse" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="TransDetailResponse">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="Notes" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CCSale">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TaxIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PaymentDesc" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchZIP" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchCustPNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MCC" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentOf" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSConditionCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EComInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="AuthCharInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardCertData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CAVVData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCSaleResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CCSaleResult" type="tns:CCSaleDebitResponse" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="CCSaleDebitResponse">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CVV2Code" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ACI" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthSource" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="TransactionIdentifier" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ValidationCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CAVVResultCode" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CCSaleRSP">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TaxIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PaymentDesc" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchZIP" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchCustPNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MCC" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentOf" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSConditionCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EComInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="AuthCharInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardCertData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CAVVData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCSaleRSPResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CCSaleRSPResult" type="tns:CCSaleDebitResponseRSP" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="CCSaleDebitResponseRSP">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CVV2Code" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ACI" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthSource" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="TransactionIdentifier" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ValidationCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CAVVResultCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="ResponseCode" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CCSaleRSPWithShipping">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TaxIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ShipToZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PaymentDesc" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchZIP" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchCustPNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MCC" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentOf" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSConditionCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EComInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="AuthCharInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardCertData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CAVVData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCSaleRSPWithShippingResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CCSaleRSPWithShippingResult" type="tns:CCSaleDebitResponseRSP" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCSaleRSPTransit">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Amount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TaxIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SaleTaxAmount" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PONumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardNumber" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardHolderName" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Expiration" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CVV2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="Address" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZipCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PaymentDesc" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchZIP" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MerchCustPNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="MCC" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentNum" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="InstallmentOf" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="POSConditionCode" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EComInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="AuthCharInd" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CardCertData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="CAVVData" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="UserId" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransitTypeIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransitModeIndicator" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TrackData" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCSaleRSPTransitResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CCSaleRSPTransitResult" type="tns:CCSaleDebitResponseRSP" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardAutoRefundorVoid">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CreditCardAutoRefundorVoidResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CreditCardAutoRefundorVoidResult" type="tns:BankCardRefundStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="BankCardRefundStatus">
        <s:sequence>
          <s:element minOccurs="1" maxOccurs="1" name="TransID" type="s:int" />
          <s:element minOccurs="1" maxOccurs="1" name="CreditID" type="s:int" />
          <s:element minOccurs="0" maxOccurs="1" name="RefID" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="PostedDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="SettledDate" type="s:dateTime" />
          <s:element minOccurs="1" maxOccurs="1" name="Amount" type="s:decimal" />
          <s:element minOccurs="0" maxOccurs="1" name="AuthCode" type="s:string" />
          <s:element minOccurs="1" maxOccurs="1" name="Status" type="tns:StatusCode" />
          <s:element minOccurs="0" maxOccurs="1" name="AVSCode" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="Message" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="CVV2Code" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="CCAutoRefundorVoidSuspectedFraud">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="MerchantID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="RegKey" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TransID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="SuspectFraud" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="CCAutoRefundorVoidSuspectedFraudResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="CCAutoRefundorVoidSuspectedFraudResult" type="tns:BankCardRefundStatus" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="BankCardSettleStatus" nillable="true" type="tns:BankCardSettleStatus" />
      <s:element name="ReleaseBatchSettleStatus" nillable="true" type="tns:ReleaseBatchSettleStatus" />
      <s:element name="AccountVerificationResponse" nillable="true" type="tns:AccountVerificationResponse" />
      <s:element name="BankCardDebitStatus" nillable="true" type="tns:BankCardDebitStatus" />
      <s:element name="BankCardCreditStatus" nillable="true" type="tns:BankCardCreditStatus" />
      <s:element name="BankCardDebitStatusRsp" nillable="true" type="tns:BankCardDebitStatusRsp" />
      <s:element name="TransDetailResponse" nillable="true" type="tns:TransDetailResponse" />
      <s:element name="CCSaleDebitResponse" nillable="true" type="tns:CCSaleDebitResponse" />
      <s:element name="CCSaleDebitResponseRSP" nillable="true" type="tns:CCSaleDebitResponseRSP" />
      <s:element name="BankCardRefundStatus" nillable="true" type="tns:BankCardRefundStatus" />
    </s:schema>
  </wsdl:types>
  <wsdl:message name="CloseBatchSoapIn">
    <wsdl:part name="parameters" element="tns:CloseBatch" />
  </wsdl:message>
  <wsdl:message name="CloseBatchSoapOut">
    <wsdl:part name="parameters" element="tns:CloseBatchResponse" />
  </wsdl:message>
  <wsdl:message name="ReleaseBatchSoapIn">
    <wsdl:part name="parameters" element="tns:ReleaseBatch" />
  </wsdl:message>
  <wsdl:message name="ReleaseBatchSoapOut">
    <wsdl:part name="parameters" element="tns:ReleaseBatchResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoSettleSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardAutoSettle" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoSettleSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardAutoSettleResponse" />
  </wsdl:message>
  <wsdl:message name="VerifyAccountSoapIn">
    <wsdl:part name="parameters" element="tns:VerifyAccount" />
  </wsdl:message>
  <wsdl:message name="VerifyAccountSoapOut">
    <wsdl:part name="parameters" element="tns:VerifyAccountResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardSale" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardSaleResponse" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleSoapIn">
    <wsdl:part name="parameters" element="tns:DebitCardSale" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleSoapOut">
    <wsdl:part name="parameters" element="tns:DebitCardSaleResponse" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleTransitSoapIn">
    <wsdl:part name="parameters" element="tns:DebitCardSaleTransit" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleTransitSoapOut">
    <wsdl:part name="parameters" element="tns:DebitCardSaleTransitResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleUserSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardSaleUser" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleUserSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardSaleUserResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardVoiceAuthorization" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardVoiceAuthorizationResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationUserSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardVoiceAuthorizationUser" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationUserSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardVoiceAuthorizationUserResponse" />
  </wsdl:message>
  <wsdl:message name="BlindCreditSoapIn">
    <wsdl:part name="parameters" element="tns:BlindCredit" />
  </wsdl:message>
  <wsdl:message name="BlindCreditSoapOut">
    <wsdl:part name="parameters" element="tns:BlindCreditResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardCreditSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardCredit" />
  </wsdl:message>
  <wsdl:message name="CreditCardCreditSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardCreditResponse" />
  </wsdl:message>
  <wsdl:message name="DebitCardCreditSoapIn">
    <wsdl:part name="parameters" element="tns:DebitCardCredit" />
  </wsdl:message>
  <wsdl:message name="DebitCardCreditSoapOut">
    <wsdl:part name="parameters" element="tns:DebitCardCreditResponse" />
  </wsdl:message>
  <wsdl:message name="VoidCreditCardSaleSoapIn">
    <wsdl:part name="parameters" element="tns:VoidCreditCardSale" />
  </wsdl:message>
  <wsdl:message name="VoidCreditCardSaleSoapOut">
    <wsdl:part name="parameters" element="tns:VoidCreditCardSaleResponse" />
  </wsdl:message>
  <wsdl:message name="VoidDebitCardSaleSoapIn">
    <wsdl:part name="parameters" element="tns:VoidDebitCardSale" />
  </wsdl:message>
  <wsdl:message name="VoidDebitCardSaleSoapOut">
    <wsdl:part name="parameters" element="tns:VoidDebitCardSaleResponse" />
  </wsdl:message>
  <wsdl:message name="CCVoidSaleSuspectedFraudSoapIn">
    <wsdl:part name="parameters" element="tns:CCVoidSaleSuspectedFraud" />
  </wsdl:message>
  <wsdl:message name="CCVoidSaleSuspectedFraudSoapOut">
    <wsdl:part name="parameters" element="tns:CCVoidSaleSuspectedFraudResponse" />
  </wsdl:message>
  <wsdl:message name="GetBankCardDebitStatusSoapIn">
    <wsdl:part name="parameters" element="tns:GetBankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="GetBankCardDebitStatusSoapOut">
    <wsdl:part name="parameters" element="tns:GetBankCardDebitStatusResponse" />
  </wsdl:message>
  <wsdl:message name="GetBankCardCreditStatusSoapIn">
    <wsdl:part name="parameters" element="tns:GetBankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="GetBankCardCreditStatusSoapOut">
    <wsdl:part name="parameters" element="tns:GetBankCardCreditStatusResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardSaleAll" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardSaleAllResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllTransitSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardSaleAllTransit" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllTransitSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardSaleAllTransitResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllRSPSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardSaleAllRSP" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllRSPSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardSaleAllRSPResponse" />
  </wsdl:message>
  <wsdl:message name="GetTransactionDetailsSoapIn">
    <wsdl:part name="parameters" element="tns:GetTransactionDetails" />
  </wsdl:message>
  <wsdl:message name="GetTransactionDetailsSoapOut">
    <wsdl:part name="parameters" element="tns:GetTransactionDetailsResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleSoapIn">
    <wsdl:part name="parameters" element="tns:CCSale" />
  </wsdl:message>
  <wsdl:message name="CCSaleSoapOut">
    <wsdl:part name="parameters" element="tns:CCSaleResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPSoapIn">
    <wsdl:part name="parameters" element="tns:CCSaleRSP" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPSoapOut">
    <wsdl:part name="parameters" element="tns:CCSaleRSPResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPWithShippingSoapIn">
    <wsdl:part name="parameters" element="tns:CCSaleRSPWithShipping" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPWithShippingSoapOut">
    <wsdl:part name="parameters" element="tns:CCSaleRSPWithShippingResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPTransitSoapIn">
    <wsdl:part name="parameters" element="tns:CCSaleRSPTransit" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPTransitSoapOut">
    <wsdl:part name="parameters" element="tns:CCSaleRSPTransitResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoRefundorVoidSoapIn">
    <wsdl:part name="parameters" element="tns:CreditCardAutoRefundorVoid" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoRefundorVoidSoapOut">
    <wsdl:part name="parameters" element="tns:CreditCardAutoRefundorVoidResponse" />
  </wsdl:message>
  <wsdl:message name="CCAutoRefundorVoidSuspectedFraudSoapIn">
    <wsdl:part name="parameters" element="tns:CCAutoRefundorVoidSuspectedFraud" />
  </wsdl:message>
  <wsdl:message name="CCAutoRefundorVoidSuspectedFraudSoapOut">
    <wsdl:part name="parameters" element="tns:CCAutoRefundorVoidSuspectedFraudResponse" />
  </wsdl:message>
  <wsdl:message name="CloseBatchHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="ForceSettlement" type="s:string" />
    <wsdl:part name="CreditID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CloseBatchHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardSettleStatus" />
  </wsdl:message>
  <wsdl:message name="ReleaseBatchHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ReleaseBatchHttpGetOut">
    <wsdl:part name="Body" element="tns:ReleaseBatchSettleStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoSettleHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoSettleHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardSettleStatus" />
  </wsdl:message>
  <wsdl:message name="VerifyAccountHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="VerifyAccountHttpGetOut">
    <wsdl:part name="Body" element="tns:AccountVerificationResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="EncryptedPIN" type="s:string" />
    <wsdl:part name="SMID" type="s:string" />
    <wsdl:part name="PINFormat" type="s:string" />
    <wsdl:part name="CashbackAmount" type="s:string" />
    <wsdl:part name="VoucherNumber" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleTransitHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="EncryptedPIN" type="s:string" />
    <wsdl:part name="SMID" type="s:string" />
    <wsdl:part name="PINFormat" type="s:string" />
    <wsdl:part name="CashbackAmount" type="s:string" />
    <wsdl:part name="VoucherNumber" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TransitTypeIndicator" type="s:string" />
    <wsdl:part name="TransitModeIndicator" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleTransitHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleUserHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="sUserID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleUserHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="VoiceAuthCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationUserHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="VoiceAuthCode" type="s:string" />
    <wsdl:part name="sUserID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationUserHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="BlindCreditHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="BlindCreditHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardCreditHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardCreditHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="DebitCardCreditHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DebitCardCreditHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="VoidCreditCardSaleHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="VoidCreditCardSaleHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="VoidDebitCardSaleHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="VoidDebitCardSaleHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CCVoidSaleSuspectedFraudHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="SuspectFraud" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCVoidSaleSuspectedFraudHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="GetBankCardDebitStatusHttpGetIn">
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetBankCardDebitStatusHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="GetBankCardCreditStatusHttpGetIn">
    <wsdl:part name="CreditID" type="s:string" />
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetBankCardCreditStatusHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef1" type="s:string" />
    <wsdl:part name="UsrDef2" type="s:string" />
    <wsdl:part name="UsrDef3" type="s:string" />
    <wsdl:part name="UsrDef4" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllTransitHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="TransitTypeIndicator" type="s:string" />
    <wsdl:part name="TransitModeIndicator" type="s:string" />
    <wsdl:part name="UsrDef1" type="s:string" />
    <wsdl:part name="UsrDef2" type="s:string" />
    <wsdl:part name="UsrDef3" type="s:string" />
    <wsdl:part name="UsrDef4" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllTransitHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllRSPHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef1" type="s:string" />
    <wsdl:part name="UsrDef2" type="s:string" />
    <wsdl:part name="UsrDef3" type="s:string" />
    <wsdl:part name="UsrDef4" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllRSPHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatusRsp" />
  </wsdl:message>
  <wsdl:message name="GetTransactionDetailsHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="PostedDate" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetTransactionDetailsHttpGetOut">
    <wsdl:part name="Body" element="tns:TransDetailResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleHttpGetOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPHttpGetOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponseRSP" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPWithShippingHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="ShipToZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPWithShippingHttpGetOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponseRSP" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPTransitHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TransitTypeIndicator" type="s:string" />
    <wsdl:part name="TransitModeIndicator" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPTransitHttpGetOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponseRSP" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoRefundorVoidHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoRefundorVoidHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardRefundStatus" />
  </wsdl:message>
  <wsdl:message name="CCAutoRefundorVoidSuspectedFraudHttpGetIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="SuspectFraud" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCAutoRefundorVoidSuspectedFraudHttpGetOut">
    <wsdl:part name="Body" element="tns:BankCardRefundStatus" />
  </wsdl:message>
  <wsdl:message name="CloseBatchHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="ForceSettlement" type="s:string" />
    <wsdl:part name="CreditID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CloseBatchHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardSettleStatus" />
  </wsdl:message>
  <wsdl:message name="ReleaseBatchHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
  </wsdl:message>
  <wsdl:message name="ReleaseBatchHttpPostOut">
    <wsdl:part name="Body" element="tns:ReleaseBatchSettleStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoSettleHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoSettleHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardSettleStatus" />
  </wsdl:message>
  <wsdl:message name="VerifyAccountHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="VerifyAccountHttpPostOut">
    <wsdl:part name="Body" element="tns:AccountVerificationResponse" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="EncryptedPIN" type="s:string" />
    <wsdl:part name="SMID" type="s:string" />
    <wsdl:part name="PINFormat" type="s:string" />
    <wsdl:part name="CashbackAmount" type="s:string" />
    <wsdl:part name="VoucherNumber" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleTransitHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="EncryptedPIN" type="s:string" />
    <wsdl:part name="SMID" type="s:string" />
    <wsdl:part name="PINFormat" type="s:string" />
    <wsdl:part name="CashbackAmount" type="s:string" />
    <wsdl:part name="VoucherNumber" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TransitTypeIndicator" type="s:string" />
    <wsdl:part name="TransitModeIndicator" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DebitCardSaleTransitHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleUserHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="sUserID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleUserHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="VoiceAuthCode" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationUserHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="VoiceAuthCode" type="s:string" />
    <wsdl:part name="sUserID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardVoiceAuthorizationUserHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="BlindCreditHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="BlindCreditHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardCreditHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardCreditHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="DebitCardCreditHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="DebitCardCreditHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="VoidCreditCardSaleHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="VoidCreditCardSaleHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="VoidDebitCardSaleHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="VoidDebitCardSaleHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CCVoidSaleSuspectedFraudHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="SuspectFraud" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCVoidSaleSuspectedFraudHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="GetBankCardDebitStatusHttpPostIn">
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetBankCardDebitStatusHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="GetBankCardCreditStatusHttpPostIn">
    <wsdl:part name="CreditID" type="s:string" />
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetBankCardCreditStatusHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardCreditStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef1" type="s:string" />
    <wsdl:part name="UsrDef2" type="s:string" />
    <wsdl:part name="UsrDef3" type="s:string" />
    <wsdl:part name="UsrDef4" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllTransitHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="TransitTypeIndicator" type="s:string" />
    <wsdl:part name="TransitModeIndicator" type="s:string" />
    <wsdl:part name="UsrDef1" type="s:string" />
    <wsdl:part name="UsrDef2" type="s:string" />
    <wsdl:part name="UsrDef3" type="s:string" />
    <wsdl:part name="UsrDef4" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllTransitHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatus" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllRSPHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="UserID" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
    <wsdl:part name="UsrDef1" type="s:string" />
    <wsdl:part name="UsrDef2" type="s:string" />
    <wsdl:part name="UsrDef3" type="s:string" />
    <wsdl:part name="UsrDef4" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardSaleAllRSPHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardDebitStatusRsp" />
  </wsdl:message>
  <wsdl:message name="GetTransactionDetailsHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="PostedDate" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
  </wsdl:message>
  <wsdl:message name="GetTransactionDetailsHttpPostOut">
    <wsdl:part name="Body" element="tns:TransDetailResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleHttpPostOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponse" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPHttpPostOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponseRSP" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPWithShippingHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="ShipToZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPWithShippingHttpPostOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponseRSP" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPTransitHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="Amount" type="s:string" />
    <wsdl:part name="TaxIndicator" type="s:string" />
    <wsdl:part name="SaleTaxAmount" type="s:string" />
    <wsdl:part name="PONumber" type="s:string" />
    <wsdl:part name="CardNumber" type="s:string" />
    <wsdl:part name="CardHolderName" type="s:string" />
    <wsdl:part name="Expiration" type="s:string" />
    <wsdl:part name="CVV2" type="s:string" />
    <wsdl:part name="RefID" type="s:string" />
    <wsdl:part name="Address" type="s:string" />
    <wsdl:part name="ZipCode" type="s:string" />
    <wsdl:part name="PaymentDesc" type="s:string" />
    <wsdl:part name="MerchZIP" type="s:string" />
    <wsdl:part name="MerchCustPNum" type="s:string" />
    <wsdl:part name="MCC" type="s:string" />
    <wsdl:part name="InstallmentNum" type="s:string" />
    <wsdl:part name="InstallmentOf" type="s:string" />
    <wsdl:part name="POSInd" type="s:string" />
    <wsdl:part name="POSConditionCode" type="s:string" />
    <wsdl:part name="EComInd" type="s:string" />
    <wsdl:part name="AuthCharInd" type="s:string" />
    <wsdl:part name="CardCertData" type="s:string" />
    <wsdl:part name="CAVVData" type="s:string" />
    <wsdl:part name="UserId" type="s:string" />
    <wsdl:part name="TransitTypeIndicator" type="s:string" />
    <wsdl:part name="TransitModeIndicator" type="s:string" />
    <wsdl:part name="TrackData" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCSaleRSPTransitHttpPostOut">
    <wsdl:part name="Body" element="tns:CCSaleDebitResponseRSP" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoRefundorVoidHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CreditCardAutoRefundorVoidHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardRefundStatus" />
  </wsdl:message>
  <wsdl:message name="CCAutoRefundorVoidSuspectedFraudHttpPostIn">
    <wsdl:part name="MerchantID" type="s:string" />
    <wsdl:part name="RegKey" type="s:string" />
    <wsdl:part name="TransID" type="s:string" />
    <wsdl:part name="SuspectFraud" type="s:string" />
  </wsdl:message>
  <wsdl:message name="CCAutoRefundorVoidSuspectedFraudHttpPostOut">
    <wsdl:part name="Body" element="tns:BankCardRefundStatus" />
  </wsdl:message>
  <wsdl:portType name="CreditCardSoap">
    <wsdl:operation name="CloseBatch">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use.CreditID is only required for closing batch for Credit. A CreditID will be returned when a Credit Card Credit is authrorized with WebMethod CreditCardCredit.Credit Amounts can not exceed the original amount. </wsdl:documentation>
      <wsdl:input message="tns:CloseBatchSoapIn" />
      <wsdl:output message="tns:CloseBatchSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey </wsdl:documentation>
      <wsdl:input message="tns:ReleaseBatchSoapIn" />
      <wsdl:output message="tns:ReleaseBatchSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardAutoSettleSoapIn" />
      <wsdl:output message="tns:CreditCardAutoSettleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Account Verifications. Required Fields are MerchantID, RegKey, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:VerifyAccountSoapIn" />
      <wsdl:output message="tns:VerifyAccountSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleSoapIn" />
      <wsdl:output message="tns:CreditCardSaleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For debit card authorizations. Required fields are MerchantID, RegKey, Amount, CardNumber, Expiration, EncryptedPIN, SMID, PINFormat, TrackData (always should be Track2).VoucherNumber is required for EBT/FoodStamp transaction.RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. Possible values for PINFormat are (01 = ANSI standard ,02 = PIN length/PIN/fill characters, 03 = PIN/Fill characters).SMID = System Management Information Data Commonly referred to as DUKPT - (Derived Unique Key Per Transaction) is a 12 or 20 character data block containing a Base Derivation Key (BDK) ID, Terminal ID and Transaction Counter.UsrDef is reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:DebitCardSaleSoapIn" />
      <wsdl:output message="tns:DebitCardSaleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For debit card authorizations. Required fields are MerchantID, RegKey, Amount, CardNumber, Expiration, EncryptedPIN, SMID, PINFormat, TrackData (always should be Track2).VoucherNumber is required for EBT/FoodStamp transaction.RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. Possible values for PINFormat are (01 = ANSI standard ,02 = PIN length/PIN/fill characters, 03 = PIN/Fill characters).SMID = System Management Information Data Commonly referred to as DUKPT - (Derived Unique Key Per Transaction) is a 12 or 20 character data block containing a Base Derivation Key (BDK) ID, Terminal ID and Transaction Counter.TransitTypeIndicator and TransitMode Indicator are optional fields and Possible values for TransitTypeIndicator is 01-99 and Possible values for TransitModeIndicator is 00-99.UsrDef is reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:DebitCardSaleTransitSoapIn" />
      <wsdl:output message="tns:DebitCardSaleTransitSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleUserSoapIn" />
      <wsdl:output message="tns:CreditCardSaleUserSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardVoiceAuthorizationSoapIn" />
      <wsdl:output message="tns:CreditCardVoiceAuthorizationSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardVoiceAuthorizationUserSoapIn" />
      <wsdl:output message="tns:CreditCardVoiceAuthorizationUserSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card credit processing. Required Fields are MerchantID, RegKey, Amount, Card Number, Expiration Date. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use</wsdl:documentation>
      <wsdl:input message="tns:BlindCreditSoapIn" />
      <wsdl:output message="tns:BlindCreditSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card credit processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardCreditSoapIn" />
      <wsdl:output message="tns:CreditCardCreditSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Debit card credit processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Debit Card Sale is authrorized with WebMethod DebitCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:DebitCardCreditSoapIn" />
      <wsdl:output message="tns:DebitCardCreditSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a credit card sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. A Credit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:VoidCreditCardSaleSoapIn" />
      <wsdl:output message="tns:VoidCreditCardSaleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a Debit card sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Debit Card Sale is authrorized with WebMethod DebitCardSale. A Debit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:VoidDebitCardSaleSoapIn" />
      <wsdl:output message="tns:VoidDebitCardSaleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a credit card sale or Debit Card Sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. Possible values for SuspectFraud are Y (TRUE) or N (FALSE).  A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. A Credit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:CCVoidSaleSuspectedFraudSoapIn" />
      <wsdl:output message="tns:CCVoidSaleSuspectedFraudSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Determining the current status of a credit card sale.</wsdl:documentation>
      <wsdl:input message="tns:GetBankCardDebitStatusSoapIn" />
      <wsdl:output message="tns:GetBankCardDebitStatusSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Determining the current status of a credit card credit</wsdl:documentation>
      <wsdl:input message="tns:GetBankCardCreditStatusSoapIn" />
      <wsdl:output message="tns:GetBankCardCreditStatusSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID are optional fields.UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllSoapIn" />
      <wsdl:output message="tns:CreditCardSaleAllSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID,TransitTypeIndicator and  TransitModeIndicator are optional fields.The Possible values for TransitTypeIndicator is between 01-99 and  TransitModeIndicator is between 00-99UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllTransitSoapIn" />
      <wsdl:output message="tns:CreditCardSaleAllTransitSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID are optional fields.UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllRSPSoapIn" />
      <wsdl:output message="tns:CreditCardSaleAllRSPSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card authorizations. Required Fields are MerchantID, RegKey,RefID, PostedDate and Amount. PostedDate is in the form of either MM/DD/YY or MM/DD/YYYY ie 01/12/09 or 01/12/2009.</wsdl:documentation>
      <wsdl:input message="tns:GetTransactionDetailsSoapIn" />
      <wsdl:output message="tns:GetTransactionDetailsSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleSoapIn" />
      <wsdl:output message="tns:CCSaleSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPSoapIn" />
      <wsdl:output message="tns:CCSaleRSPSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPWithShippingSoapIn" />
      <wsdl:output message="tns:CCSaleRSPWithShippingSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator,TransitTypeIndicator and TransitMode Indicator are optional fields.The Possible values for TransitTypeIndicator is between 01-99 and  TransitModeIndicator is between 00-99</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPTransitSoapIn" />
      <wsdl:output message="tns:CCSaleRSPTransitSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Auto Refund or Void Required Fields are MerchantID, RegKey and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardAutoRefundorVoid. A CreditID will be returned when a Credit Card Credit is authorized. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardAutoRefundorVoidSoapIn" />
      <wsdl:output message="tns:CreditCardAutoRefundorVoidSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Auto Refund or Void Required Fields are MerchantID, RegKey and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardAutoRefundorVoid. A CreditID will be returned when a Credit Card Credit is authorized. </wsdl:documentation>
      <wsdl:input message="tns:CCAutoRefundorVoidSuspectedFraudSoapIn" />
      <wsdl:output message="tns:CCAutoRefundorVoidSuspectedFraudSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:portType name="CreditCardHttpGet">
    <wsdl:operation name="CloseBatch">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use.CreditID is only required for closing batch for Credit. A CreditID will be returned when a Credit Card Credit is authrorized with WebMethod CreditCardCredit.Credit Amounts can not exceed the original amount. </wsdl:documentation>
      <wsdl:input message="tns:CloseBatchHttpGetIn" />
      <wsdl:output message="tns:CloseBatchHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey </wsdl:documentation>
      <wsdl:input message="tns:ReleaseBatchHttpGetIn" />
      <wsdl:output message="tns:ReleaseBatchHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardAutoSettleHttpGetIn" />
      <wsdl:output message="tns:CreditCardAutoSettleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Account Verifications. Required Fields are MerchantID, RegKey, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:VerifyAccountHttpGetIn" />
      <wsdl:output message="tns:VerifyAccountHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleHttpGetIn" />
      <wsdl:output message="tns:CreditCardSaleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For debit card authorizations. Required fields are MerchantID, RegKey, Amount, CardNumber, Expiration, EncryptedPIN, SMID, PINFormat, TrackData (always should be Track2).VoucherNumber is required for EBT/FoodStamp transaction.RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. Possible values for PINFormat are (01 = ANSI standard ,02 = PIN length/PIN/fill characters, 03 = PIN/Fill characters).SMID = System Management Information Data Commonly referred to as DUKPT - (Derived Unique Key Per Transaction) is a 12 or 20 character data block containing a Base Derivation Key (BDK) ID, Terminal ID and Transaction Counter.UsrDef is reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:DebitCardSaleHttpGetIn" />
      <wsdl:output message="tns:DebitCardSaleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For debit card authorizations. Required fields are MerchantID, RegKey, Amount, CardNumber, Expiration, EncryptedPIN, SMID, PINFormat, TrackData (always should be Track2).VoucherNumber is required for EBT/FoodStamp transaction.RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. Possible values for PINFormat are (01 = ANSI standard ,02 = PIN length/PIN/fill characters, 03 = PIN/Fill characters).SMID = System Management Information Data Commonly referred to as DUKPT - (Derived Unique Key Per Transaction) is a 12 or 20 character data block containing a Base Derivation Key (BDK) ID, Terminal ID and Transaction Counter.TransitTypeIndicator and TransitMode Indicator are optional fields and Possible values for TransitTypeIndicator is 01-99 and Possible values for TransitModeIndicator is 00-99.UsrDef is reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:DebitCardSaleTransitHttpGetIn" />
      <wsdl:output message="tns:DebitCardSaleTransitHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleUserHttpGetIn" />
      <wsdl:output message="tns:CreditCardSaleUserHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardVoiceAuthorizationHttpGetIn" />
      <wsdl:output message="tns:CreditCardVoiceAuthorizationHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardVoiceAuthorizationUserHttpGetIn" />
      <wsdl:output message="tns:CreditCardVoiceAuthorizationUserHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card credit processing. Required Fields are MerchantID, RegKey, Amount, Card Number, Expiration Date. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use</wsdl:documentation>
      <wsdl:input message="tns:BlindCreditHttpGetIn" />
      <wsdl:output message="tns:BlindCreditHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card credit processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardCreditHttpGetIn" />
      <wsdl:output message="tns:CreditCardCreditHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Debit card credit processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Debit Card Sale is authrorized with WebMethod DebitCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:DebitCardCreditHttpGetIn" />
      <wsdl:output message="tns:DebitCardCreditHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a credit card sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. A Credit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:VoidCreditCardSaleHttpGetIn" />
      <wsdl:output message="tns:VoidCreditCardSaleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a Debit card sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Debit Card Sale is authrorized with WebMethod DebitCardSale. A Debit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:VoidDebitCardSaleHttpGetIn" />
      <wsdl:output message="tns:VoidDebitCardSaleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a credit card sale or Debit Card Sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. Possible values for SuspectFraud are Y (TRUE) or N (FALSE).  A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. A Credit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:CCVoidSaleSuspectedFraudHttpGetIn" />
      <wsdl:output message="tns:CCVoidSaleSuspectedFraudHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Determining the current status of a credit card sale.</wsdl:documentation>
      <wsdl:input message="tns:GetBankCardDebitStatusHttpGetIn" />
      <wsdl:output message="tns:GetBankCardDebitStatusHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Determining the current status of a credit card credit</wsdl:documentation>
      <wsdl:input message="tns:GetBankCardCreditStatusHttpGetIn" />
      <wsdl:output message="tns:GetBankCardCreditStatusHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID are optional fields.UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllHttpGetIn" />
      <wsdl:output message="tns:CreditCardSaleAllHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID,TransitTypeIndicator and  TransitModeIndicator are optional fields.The Possible values for TransitTypeIndicator is between 01-99 and  TransitModeIndicator is between 00-99UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllTransitHttpGetIn" />
      <wsdl:output message="tns:CreditCardSaleAllTransitHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID are optional fields.UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllRSPHttpGetIn" />
      <wsdl:output message="tns:CreditCardSaleAllRSPHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card authorizations. Required Fields are MerchantID, RegKey,RefID, PostedDate and Amount. PostedDate is in the form of either MM/DD/YY or MM/DD/YYYY ie 01/12/09 or 01/12/2009.</wsdl:documentation>
      <wsdl:input message="tns:GetTransactionDetailsHttpGetIn" />
      <wsdl:output message="tns:GetTransactionDetailsHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleHttpGetIn" />
      <wsdl:output message="tns:CCSaleHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPHttpGetIn" />
      <wsdl:output message="tns:CCSaleRSPHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPWithShippingHttpGetIn" />
      <wsdl:output message="tns:CCSaleRSPWithShippingHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator,TransitTypeIndicator and TransitMode Indicator are optional fields.The Possible values for TransitTypeIndicator is between 01-99 and  TransitModeIndicator is between 00-99</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPTransitHttpGetIn" />
      <wsdl:output message="tns:CCSaleRSPTransitHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Auto Refund or Void Required Fields are MerchantID, RegKey and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardAutoRefundorVoid. A CreditID will be returned when a Credit Card Credit is authorized. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardAutoRefundorVoidHttpGetIn" />
      <wsdl:output message="tns:CreditCardAutoRefundorVoidHttpGetOut" />
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Auto Refund or Void Required Fields are MerchantID, RegKey and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardAutoRefundorVoid. A CreditID will be returned when a Credit Card Credit is authorized. </wsdl:documentation>
      <wsdl:input message="tns:CCAutoRefundorVoidSuspectedFraudHttpGetIn" />
      <wsdl:output message="tns:CCAutoRefundorVoidSuspectedFraudHttpGetOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:portType name="CreditCardHttpPost">
    <wsdl:operation name="CloseBatch">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use.CreditID is only required for closing batch for Credit. A CreditID will be returned when a Credit Card Credit is authrorized with WebMethod CreditCardCredit.Credit Amounts can not exceed the original amount. </wsdl:documentation>
      <wsdl:input message="tns:CloseBatchHttpPostIn" />
      <wsdl:output message="tns:CloseBatchHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey </wsdl:documentation>
      <wsdl:input message="tns:ReleaseBatchHttpPostIn" />
      <wsdl:output message="tns:ReleaseBatchHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card settle processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardAutoSettleHttpPostIn" />
      <wsdl:output message="tns:CreditCardAutoSettleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Account Verifications. Required Fields are MerchantID, RegKey, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:VerifyAccountHttpPostIn" />
      <wsdl:output message="tns:VerifyAccountHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleHttpPostIn" />
      <wsdl:output message="tns:CreditCardSaleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For debit card authorizations. Required fields are MerchantID, RegKey, Amount, CardNumber, Expiration, EncryptedPIN, SMID, PINFormat, TrackData (always should be Track2).VoucherNumber is required for EBT/FoodStamp transaction.RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. Possible values for PINFormat are (01 = ANSI standard ,02 = PIN length/PIN/fill characters, 03 = PIN/Fill characters).SMID = System Management Information Data Commonly referred to as DUKPT - (Derived Unique Key Per Transaction) is a 12 or 20 character data block containing a Base Derivation Key (BDK) ID, Terminal ID and Transaction Counter.UsrDef is reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:DebitCardSaleHttpPostIn" />
      <wsdl:output message="tns:DebitCardSaleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For debit card authorizations. Required fields are MerchantID, RegKey, Amount, CardNumber, Expiration, EncryptedPIN, SMID, PINFormat, TrackData (always should be Track2).VoucherNumber is required for EBT/FoodStamp transaction.RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. Possible values for PINFormat are (01 = ANSI standard ,02 = PIN length/PIN/fill characters, 03 = PIN/Fill characters).SMID = System Management Information Data Commonly referred to as DUKPT - (Derived Unique Key Per Transaction) is a 12 or 20 character data block containing a Base Derivation Key (BDK) ID, Terminal ID and Transaction Counter.TransitTypeIndicator and TransitMode Indicator are optional fields and Possible values for TransitTypeIndicator is 01-99 and Possible values for TransitModeIndicator is 00-99.UsrDef is reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:DebitCardSaleTransitHttpPostIn" />
      <wsdl:output message="tns:DebitCardSaleTransitHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleUserHttpPostIn" />
      <wsdl:output message="tns:CreditCardSaleUserHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardVoiceAuthorizationHttpPostIn" />
      <wsdl:output message="tns:CreditCardVoiceAuthorizationHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it here. TrackData is for Swiped Transactions.</wsdl:documentation>
      <wsdl:input message="tns:CreditCardVoiceAuthorizationUserHttpPostIn" />
      <wsdl:output message="tns:CreditCardVoiceAuthorizationUserHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card credit processing. Required Fields are MerchantID, RegKey, Amount, Card Number, Expiration Date. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use</wsdl:documentation>
      <wsdl:input message="tns:BlindCreditHttpPostIn" />
      <wsdl:output message="tns:BlindCreditHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card credit processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardCreditHttpPostIn" />
      <wsdl:output message="tns:CreditCardCreditHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Debit card credit processing. Required Fields are MerchantID, RegKey, Amount and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Debit Card Sale is authrorized with WebMethod DebitCardSale. RefID is for your determined use. Credit Amounts can not exceed the original sale amount. </wsdl:documentation>
      <wsdl:input message="tns:DebitCardCreditHttpPostIn" />
      <wsdl:output message="tns:DebitCardCreditHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a credit card sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. A Credit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:VoidCreditCardSaleHttpPostIn" />
      <wsdl:output message="tns:VoidCreditCardSaleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a Debit card sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Debit Card Sale is authrorized with WebMethod DebitCardSale. A Debit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:VoidDebitCardSaleHttpPostIn" />
      <wsdl:output message="tns:VoidDebitCardSaleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For voiding a credit card sale or Debit Card Sale. Required Fields are MerchantID, RegKey  and TransID. TransID is the TransID of the original sale. Possible values for SuspectFraud are Y (TRUE) or N (FALSE).  A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardSale. A Credit Card Sale can only be voided if it has not been settled. Payment Resources settles all transactions  approximately 9PM Pacific Time. To determine if a transaction is voidable(ie not settled yet) you can use the WebMethod GetBankCardDebitStatus.</wsdl:documentation>
      <wsdl:input message="tns:CCVoidSaleSuspectedFraudHttpPostIn" />
      <wsdl:output message="tns:CCVoidSaleSuspectedFraudHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Determining the current status of a credit card sale.</wsdl:documentation>
      <wsdl:input message="tns:GetBankCardDebitStatusHttpPostIn" />
      <wsdl:output message="tns:GetBankCardDebitStatusHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For Determining the current status of a credit card credit</wsdl:documentation>
      <wsdl:input message="tns:GetBankCardCreditStatusHttpPostIn" />
      <wsdl:output message="tns:GetBankCardCreditStatusHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID are optional fields.UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllHttpPostIn" />
      <wsdl:output message="tns:CreditCardSaleAllHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID,TransitTypeIndicator and  TransitModeIndicator are optional fields.The Possible values for TransitTypeIndicator is between 01-99 and  TransitModeIndicator is between 00-99UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllTransitHttpPostIn" />
      <wsdl:output message="tns:CreditCardSaleAllTransitHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. TrackData is for Swiped Transactions.&lt;br&gt;For corporate purchase card additional required Fields are SaleTaxAmount, PONumber. &lt;br&gt;CVV2, Address, ZipCode, UserID are optional fields.UsrDef1, UsrDef2, UsrDef3, UsrDef4 are reserved for feature use</wsdl:documentation>
      <wsdl:input message="tns:CreditCardSaleAllRSPHttpPostIn" />
      <wsdl:output message="tns:CreditCardSaleAllRSPHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card authorizations. Required Fields are MerchantID, RegKey,RefID, PostedDate and Amount. PostedDate is in the form of either MM/DD/YY or MM/DD/YYYY ie 01/12/09 or 01/12/2009.</wsdl:documentation>
      <wsdl:input message="tns:GetTransactionDetailsHttpPostIn" />
      <wsdl:output message="tns:GetTransactionDetailsHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleHttpPostIn" />
      <wsdl:output message="tns:CCSaleHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPHttpPostIn" />
      <wsdl:output message="tns:CCSaleRSPHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator are optional fields.</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPWithShippingHttpPostIn" />
      <wsdl:output message="tns:CCSaleRSPWithShippingHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card debit authorizations. Required Fields are MerchantID, RegKey, Amount, CardNumber and Expiration. Expiration is in the form mmyy. ie february 2003 would be 0203. RefID is for your determined use. If you have a unique tracking ID for purchases you might want to place it as RefID. CVV2, Address, ZipCode, AuthCharInd, InstallmentNum,InstallmentOf,MCC,MerchZIP,PaymentDesc,StoreNum,eCommerce,MerchCustPNum, TaxIndicator,TransitTypeIndicator and TransitMode Indicator are optional fields.The Possible values for TransitTypeIndicator is between 01-99 and  TransitModeIndicator is between 00-99</wsdl:documentation>
      <wsdl:input message="tns:CCSaleRSPTransitHttpPostIn" />
      <wsdl:output message="tns:CCSaleRSPTransitHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Auto Refund or Void Required Fields are MerchantID, RegKey and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardAutoRefundorVoid. A CreditID will be returned when a Credit Card Credit is authorized. </wsdl:documentation>
      <wsdl:input message="tns:CreditCardAutoRefundorVoidHttpPostIn" />
      <wsdl:output message="tns:CreditCardAutoRefundorVoidHttpPostOut" />
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">For credit card Auto Refund or Void Required Fields are MerchantID, RegKey and TransID. TransID is the TransID of the original sale. A TransID will be returned when a Credit Card Sale is authrorized with WebMethod CreditCardAutoRefundorVoid. A CreditID will be returned when a Credit Card Credit is authorized. </wsdl:documentation>
      <wsdl:input message="tns:CCAutoRefundorVoidSuspectedFraudHttpPostIn" />
      <wsdl:output message="tns:CCAutoRefundorVoidSuspectedFraudHttpPostOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="CreditCardSoap" type="tns:CreditCardSoap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="CloseBatch">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CloseBatch" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/ReleaseBatch" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardAutoSettle" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/VerifyAccount" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSale" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/DebitCardSale" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/DebitCardSaleTransit" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleUser" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardVoiceAuthorization" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardVoiceAuthorizationUser" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/BlindCredit" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardCredit" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/DebitCardCredit" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/VoidCreditCardSale" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/VoidDebitCardSale" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CCVoidSaleSuspectedFraud" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/GetBankCardDebitStatus" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/GetBankCardCreditStatus" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleAll" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleAllTransit" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleAllRSP" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/GetTransactionDetails" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CCSale" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CCSaleRSP" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CCSaleRSPWithShipping" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CCSaleRSPTransit" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CreditCardAutoRefundorVoid" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <soap:operation soapAction="http://www.paymentresources.com/webservices/CCAutoRefundorVoidSuspectedFraud" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="CreditCardSoap12" type="tns:CreditCardSoap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="CloseBatch">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CloseBatch" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/ReleaseBatch" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardAutoSettle" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/VerifyAccount" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSale" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/DebitCardSale" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/DebitCardSaleTransit" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleUser" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardVoiceAuthorization" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardVoiceAuthorizationUser" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/BlindCredit" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardCredit" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/DebitCardCredit" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/VoidCreditCardSale" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/VoidDebitCardSale" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CCVoidSaleSuspectedFraud" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/GetBankCardDebitStatus" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/GetBankCardCreditStatus" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleAll" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleAllTransit" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardSaleAllRSP" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/GetTransactionDetails" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CCSale" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CCSaleRSP" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CCSaleRSPWithShipping" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CCSaleRSPTransit" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CreditCardAutoRefundorVoid" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <soap12:operation soapAction="http://www.paymentresources.com/webservices/CCAutoRefundorVoidSuspectedFraud" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="CreditCardHttpGet" type="tns:CreditCardHttpGet">
    <http:binding verb="GET" />
    <wsdl:operation name="CloseBatch">
      <http:operation location="/CloseBatch" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <http:operation location="/ReleaseBatch" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <http:operation location="/CreditCardAutoSettle" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <http:operation location="/VerifyAccount" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <http:operation location="/CreditCardSale" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <http:operation location="/DebitCardSale" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <http:operation location="/DebitCardSaleTransit" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <http:operation location="/CreditCardSaleUser" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <http:operation location="/CreditCardVoiceAuthorization" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <http:operation location="/CreditCardVoiceAuthorizationUser" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <http:operation location="/BlindCredit" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <http:operation location="/CreditCardCredit" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <http:operation location="/DebitCardCredit" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <http:operation location="/VoidCreditCardSale" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <http:operation location="/VoidDebitCardSale" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <http:operation location="/CCVoidSaleSuspectedFraud" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <http:operation location="/GetBankCardDebitStatus" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <http:operation location="/GetBankCardCreditStatus" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <http:operation location="/CreditCardSaleAll" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <http:operation location="/CreditCardSaleAllTransit" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <http:operation location="/CreditCardSaleAllRSP" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <http:operation location="/GetTransactionDetails" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <http:operation location="/CCSale" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <http:operation location="/CCSaleRSP" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <http:operation location="/CCSaleRSPWithShipping" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <http:operation location="/CCSaleRSPTransit" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <http:operation location="/CreditCardAutoRefundorVoid" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <http:operation location="/CCAutoRefundorVoidSuspectedFraud" />
      <wsdl:input>
        <http:urlEncoded />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="CreditCardHttpPost" type="tns:CreditCardHttpPost">
    <http:binding verb="POST" />
    <wsdl:operation name="CloseBatch">
      <http:operation location="/CloseBatch" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="ReleaseBatch">
      <http:operation location="/ReleaseBatch" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoSettle">
      <http:operation location="/CreditCardAutoSettle" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VerifyAccount">
      <http:operation location="/VerifyAccount" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSale">
      <http:operation location="/CreditCardSale" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSale">
      <http:operation location="/DebitCardSale" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardSaleTransit">
      <http:operation location="/DebitCardSaleTransit" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleUser">
      <http:operation location="/CreditCardSaleUser" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorization">
      <http:operation location="/CreditCardVoiceAuthorization" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardVoiceAuthorizationUser">
      <http:operation location="/CreditCardVoiceAuthorizationUser" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="BlindCredit">
      <http:operation location="/BlindCredit" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardCredit">
      <http:operation location="/CreditCardCredit" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="DebitCardCredit">
      <http:operation location="/DebitCardCredit" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidCreditCardSale">
      <http:operation location="/VoidCreditCardSale" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="VoidDebitCardSale">
      <http:operation location="/VoidDebitCardSale" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCVoidSaleSuspectedFraud">
      <http:operation location="/CCVoidSaleSuspectedFraud" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardDebitStatus">
      <http:operation location="/GetBankCardDebitStatus" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetBankCardCreditStatus">
      <http:operation location="/GetBankCardCreditStatus" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAll">
      <http:operation location="/CreditCardSaleAll" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllTransit">
      <http:operation location="/CreditCardSaleAllTransit" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardSaleAllRSP">
      <http:operation location="/CreditCardSaleAllRSP" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="GetTransactionDetails">
      <http:operation location="/GetTransactionDetails" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSale">
      <http:operation location="/CCSale" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSP">
      <http:operation location="/CCSaleRSP" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPWithShipping">
      <http:operation location="/CCSaleRSPWithShipping" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCSaleRSPTransit">
      <http:operation location="/CCSaleRSPTransit" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CreditCardAutoRefundorVoid">
      <http:operation location="/CreditCardAutoRefundorVoid" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="CCAutoRefundorVoidSuspectedFraud">
      <http:operation location="/CCAutoRefundorVoidSuspectedFraud" />
      <wsdl:input>
        <mime:content type="application/x-www-form-urlencoded" />
      </wsdl:input>
      <wsdl:output>
        <mime:mimeXml part="Body" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="CreditCard">
    <wsdl:documentation xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">&lt;font size='4'&gt;&lt;a href='http://www.paymentresource.com'&gt;Payment Resources International&lt;/a&gt;&lt;/font&gt; &lt;font size='3'&gt;provides this Credit Card Processing Web Service for its customers. &lt;BR&gt;In order to process a credit card you will need to have a valid Merchant Account with us. &lt;BR&gt;However, two methods below can be tested without supplying a MerchantID nor RegKey.&lt;BR&gt;WebMethods CreditCardSale,CreditCardCredit, and VoidCreditCardSale will return sample results if you leave MerchantID and RegKey blank(you will need to fill in the other fields). CreditCardSale will return a fictitious authorization if the amount is even(ie 2.00,1.02). Otherwise it will return a decline. &lt;BR&gt; For more information on this service go to &lt;a href='CreditCardProcessing.htm'&gt;Processing Credit Cards&lt;/a&gt;&lt;/font&gt;</wsdl:documentation>
    <wsdl:port name="CreditCardSoap" binding="tns:CreditCardSoap">
      <soap:address location="https://webservices.primerchants.com/creditcard.asmx" />
    </wsdl:port>
    <wsdl:port name="CreditCardSoap12" binding="tns:CreditCardSoap12">
      <soap12:address location="https://webservices.primerchants.com/creditcard.asmx" />
    </wsdl:port>
    <wsdl:port name="CreditCardHttpGet" binding="tns:CreditCardHttpGet">
      <http:address location="https://webservices.primerchants.com/creditcard.asmx" />
    </wsdl:port>
    <wsdl:port name="CreditCardHttpPost" binding="tns:CreditCardHttpPost">
      <http:address location="https://webservices.primerchants.com/creditcard.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>