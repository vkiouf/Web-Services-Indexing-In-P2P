<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:tm="http://microsoft.com/wsdl/mime/textMatching/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/" xmlns:tns="http://e-services.doh.go.th/dohweb/" xmlns:s="http://www.w3.org/2001/XMLSchema" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:http="http://schemas.xmlsoap.org/wsdl/http/" targetNamespace="http://e-services.doh.go.th/dohweb/" xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/">
  <wsdl:types>
    <s:schema elementFormDefault="qualified" targetNamespace="http://e-services.doh.go.th/dohweb/">
      <s:element name="RequestStatusByCitizenID">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="citizen_id" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="RequestStatusByCitizenIDResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="RequestStatusByCitizenIDResult" type="tns:ArrayOfRequestStatusResult" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ArrayOfRequestStatusResult">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="unbounded" name="RequestStatusResult" nillable="true" type="tns:RequestStatusResult" />
        </s:sequence>
      </s:complexType>
      <s:complexType name="RequestStatusResult">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="1" name="CITIZEN_ID" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="REQUEST_NAME" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="REQUEST_DATE" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="HIGHWAY" type="s:string" />
          <s:element minOccurs="0" maxOccurs="1" name="REQUEST_STATUS" type="s:string" />
        </s:sequence>
      </s:complexType>
      <s:element name="RequestSubmitService">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="FORM_ID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="REQUESTER_TYPE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="REQUESTER_PRENAME" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="REQUESTER_NAME" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="REQUESTER_AGE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ATTORNEY_PRENAME" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ATTORNEY_NAME" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ATTORNEY_AGE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ATTORNEY_POSITION" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ADDRESS" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ROAD" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TUMBON" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="AMPHUR" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PROVINCE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="ZIPCODE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="TEL" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="FAX" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="EMAIL" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="REQUEST_DATE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="IDCARD" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PRV_ID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="HIGHWAY_ID" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="KM_TEXT" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="NOTICE" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="PRV_ID2" type="s:string" />
            <s:element minOccurs="0" maxOccurs="1" name="K_ID" type="s:string" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:element name="RequestSubmitServiceResponse">
        <s:complexType>
          <s:sequence>
            <s:element minOccurs="0" maxOccurs="1" name="RequestSubmitServiceResult" type="tns:ArrayOfString" />
          </s:sequence>
        </s:complexType>
      </s:element>
      <s:complexType name="ArrayOfString">
        <s:sequence>
          <s:element minOccurs="0" maxOccurs="unbounded" name="string" nillable="true" type="s:string" />
        </s:sequence>
      </s:complexType>
    </s:schema>
  </wsdl:types>
  <wsdl:message name="RequestStatusByCitizenIDSoapIn">
    <wsdl:part name="parameters" element="tns:RequestStatusByCitizenID" />
  </wsdl:message>
  <wsdl:message name="RequestStatusByCitizenIDSoapOut">
    <wsdl:part name="parameters" element="tns:RequestStatusByCitizenIDResponse" />
  </wsdl:message>
  <wsdl:message name="RequestSubmitServiceSoapIn">
    <wsdl:part name="parameters" element="tns:RequestSubmitService" />
  </wsdl:message>
  <wsdl:message name="RequestSubmitServiceSoapOut">
    <wsdl:part name="parameters" element="tns:RequestSubmitServiceResponse" />
  </wsdl:message>
  <wsdl:portType name="dohwebserviceSoap">
    <wsdl:operation name="RequestStatusByCitizenID">
      <wsdl:input message="tns:RequestStatusByCitizenIDSoapIn" />
      <wsdl:output message="tns:RequestStatusByCitizenIDSoapOut" />
    </wsdl:operation>
    <wsdl:operation name="RequestSubmitService">
      <wsdl:input message="tns:RequestSubmitServiceSoapIn" />
      <wsdl:output message="tns:RequestSubmitServiceSoapOut" />
    </wsdl:operation>
  </wsdl:portType>
  <wsdl:binding name="dohwebserviceSoap" type="tns:dohwebserviceSoap">
    <soap:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="RequestStatusByCitizenID">
      <soap:operation soapAction="http://e-services.doh.go.th/dohweb/RequestStatusByCitizenID" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="RequestSubmitService">
      <soap:operation soapAction="http://e-services.doh.go.th/dohweb/RequestSubmitService" style="document" />
      <wsdl:input>
        <soap:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:binding name="dohwebserviceSoap12" type="tns:dohwebserviceSoap">
    <soap12:binding transport="http://schemas.xmlsoap.org/soap/http" />
    <wsdl:operation name="RequestStatusByCitizenID">
      <soap12:operation soapAction="http://e-services.doh.go.th/dohweb/RequestStatusByCitizenID" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
    <wsdl:operation name="RequestSubmitService">
      <soap12:operation soapAction="http://e-services.doh.go.th/dohweb/RequestSubmitService" style="document" />
      <wsdl:input>
        <soap12:body use="literal" />
      </wsdl:input>
      <wsdl:output>
        <soap12:body use="literal" />
      </wsdl:output>
    </wsdl:operation>
  </wsdl:binding>
  <wsdl:service name="dohwebservice">
    <wsdl:port name="dohwebserviceSoap" binding="tns:dohwebserviceSoap">
      <soap:address location="http://e-services.doh.go.th/dohweb/dohwebservice.asmx" />
    </wsdl:port>
    <wsdl:port name="dohwebserviceSoap12" binding="tns:dohwebserviceSoap12">
      <soap12:address location="http://e-services.doh.go.th/dohweb/dohwebservice.asmx" />
    </wsdl:port>
  </wsdl:service>
</wsdl:definitions>