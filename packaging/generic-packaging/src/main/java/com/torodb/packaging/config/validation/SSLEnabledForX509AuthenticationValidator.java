/*
 *     This file is part of ToroDB.
 *
 *     ToroDB is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ToroDB is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with ToroDB. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */

package com.torodb.packaging.config.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.torodb.packaging.config.model.protocol.mongo.AuthMode;
import com.torodb.packaging.config.model.protocol.mongo.AbstractReplication;
import com.torodb.packaging.config.model.protocol.mongo.SSL;

public class SSLEnabledForX509AuthenticationValidator implements ConstraintValidator<SSLEnabledForX509Authentication, List<AbstractReplication>> {
	
	@Override
	public void initialize(SSLEnabledForX509Authentication constraintAnnotation) {
	}

	@Override
	public boolean isValid(List<AbstractReplication> value, ConstraintValidatorContext context) {
		if (value != null) {
			for (AbstractReplication replication : value) {
				if (replication.getAuth().getMode() == AuthMode.x509) {
				    SSL ssl = replication.getSsl();
	                if (!ssl.getEnabled() ||
                            ssl.getKeyStoreFile() == null ||
                            ssl.getKeyPassword() == null) {
	                    return false;
	                }
				}
			}
		}

		return true;
	}
}
