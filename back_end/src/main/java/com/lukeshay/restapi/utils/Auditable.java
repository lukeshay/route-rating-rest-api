package com.lukeshay.restapi.utils;

import static javax.persistence.TemporalType.TIMESTAMP;

import com.google.gson.annotations.Expose;
import java.util.Date;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<U> {

  @CreatedBy @Expose protected U createdBy;

  @Expose
  @CreatedDate
  @Temporal(TIMESTAMP)
  protected Date creationDate;

  @Expose @LastModifiedBy protected U lastModifiedBy;

  @Expose
  @LastModifiedDate
  @Temporal(TIMESTAMP)
  protected Date lastModifiedDate;

  public U getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(U createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public U getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(U lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }
}
