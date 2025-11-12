import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewActividad } from './view-actividad';

describe('ViewActividad', () => {
  let component: ViewActividad;
  let fixture: ComponentFixture<ViewActividad>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewActividad]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewActividad);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
